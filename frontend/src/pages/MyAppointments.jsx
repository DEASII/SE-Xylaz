import React, { useEffect, useState } from 'react';
import axios from "axios";

const MyAppointments = () => {
    const [barbers, setBarbers] = useState([]);
    const [appointments, setAppointments] = useState([]);
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(null);

    useEffect(() => {
        // ตรวจสอบ token ใน localStorage
        const storedToken = localStorage.getItem('token');
        const lastUser = localStorage.getItem('lastUser');
        if (storedToken) {
            setToken(storedToken);
            setUser(lastUser);
        }
    }, []);


    useEffect(() => {
        const fetchAppointmentInfo = () => {
            fetch('http://localhost:8085/appointment')
                .then(response => response.json())
                .then(data => {
                    // แปลงข้อมูลการนัดหมายที่ได้จาก API
                    const fetchedAppointments = data
                        .filter(appointment => appointment.username === user) // กรองตามชื่อผู้ใช้
                        .map(appointment => ({
                            date: appointment.appointmentDate,
                            time: appointment.appointmentTime,
                            stylistClass: appointment.barberClass,
                            name: appointment.username,
                            barberName: appointment.barberName,
                            barberProfilePicture:appointment.barberProfilePicture,
                            id:appointment.appointmentId
                        }));

                    // ตั้งค่า appointments ใหม่ตามข้อมูลที่แปลงแล้ว
                    setAppointments(fetchedAppointments);
                })
                .catch(error => console.error('Error fetching appointment info:', error));
        };

        // เรียกใช้ fetchAppointmentInfo เมื่อ user มีค่า
        if (user) {
            fetchAppointmentInfo();
        }
    }, [user]);


    useEffect(() => {
        // กรองข้อมูลการนัดหมาย
        fetchAppointmentInfo();
    }, [user]);

    const fetchAppointmentInfo = () => {
        const appointmentInfo = appointments.filter(
            (appointment) => appointment.name === user
        );
        setAppointments(appointmentInfo);
    };
    const handlePay = async (e, appointmentId) => {
        e.preventDefault();
        try {
            const response = await axios.patch('http://localhost:8085/appointment', {
                status: 'payed'  // อัปเดตสถานะเป็น "payed"
            });

            // ตรวจสอบผลลัพธ์ถ้าต้องการ
            // console.log('Payment updated successfully:', response.data);
        } catch (error) {
            console.error('Payment failed:', error.response?.data || error.message);
            alert(error.response?.data.message);
        }
    };


    return appointments &&(
        <div>
            {appointments.map((appointment, index) => (
                <div className='grid grid-cols-[1fr_2fr] gap-4 sm:flex sm:gap-6 py-2 border-b' key={index}>
                    <div>
                        <img
                            className='w-32 bg-indigo-50'
                            src={appointment.barberProfilePicture} // ใช้ profilePicture จาก barberClass
                            alt=""
                        />
                    </div>
                    <div className='flex-1 text-sm text-zinc-600'>
                        <p className='text-neutral-800 font-semibold'>{appointment.barberName}</p>
                        <p className='text-xs mt-1'>
                            <span className='text-sm text-neutral-700 font-medium'>Date & Time:</span> {appointment.date} | {appointment.time}
                        </p>
                    </div>
                    <div className='flex flex-col gap-2 justify-end'>
                        <button onClick={(e) => handlePay(e, appointment.id)} className='text-sm text-stone-500 text-center sm:min-w-48 py-2 border rounded hover:bg-primary hover:text-white transition-all duration-300'>
                            Pay Online
                        </button>
                        <button className='text-sm text-stone-500 text-center sm:min-w-48 py-2 border rounded hover:bg-red-600 hover:text-white transition-all duration-300'>Cancel appointment</button>
                    </div>
                </div>
            ))}
        </div>


    )
}

export default MyAppointments