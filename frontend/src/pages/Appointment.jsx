import React, { useContext, useEffect, useState } from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import { AppContext } from '../context/AppContext';
import { assets } from '../assets/assets';
import TopDoctors from '../components/TopDoctors';
import axios from 'axios';
import * as response from "autoprefixer";

response.data.member_id = undefined;
const Appointment = () => {
  const { docId } = useParams();
  const { doctors, currencySymbol } = useContext(AppContext);
  const daysOfWeek = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];

  // const [docInfo, setDocInfo] = useState(null);
  const [docSlots, setDocSlots] = useState([]);
  const [slotIndex, setSlotIndex] = useState(0);
  const [slotTime, setSlotTime] = useState('');
  const [barbers, setBarbers] = useState({});
  const [appointment, setAppointment] = useState({});
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [bookedAppointments, setBookedAppointments]=useState([]);


  useEffect(() => {
    // เรียกใช้ API เพื่อดึงข้อมูลบาร์เบอร์
    fetch('http://localhost:8085/')
        .then(response => response.json())
        .then(data => {
          setBarbers(data);
        })
        .catch(error => console.error('Error fetching barbers:', error));
  }, []);
  useEffect(() => {
    fetch('http://localhost:8085/appointment')
        .then(response => response.json())
        .then(data => {
          // ทำการแปลงข้อมูล appointment ที่ได้มา
          const appointments = data.map(appointment => ({
            date: appointment.appointmentDate, // วันที่
            time: appointment.appointmentTime // เวลา
          }));

          // ตั้งค่า bookedAppointments
          setBookedAppointments(appointments);
        })
        .catch(error => console.error('Error fetching appointment:', error));
  }, []);


  useEffect(() => {
    // ตรวจสอบว่ามี token ใน localStorage หรือไม่ ถ้ามีก็ดึงค่าชื่อผู้ใช้
    const storedToken = localStorage.getItem('token');
    const lastUser = localStorage.getItem('lastUser');

    if (storedToken) {
      setToken(storedToken);
      setUser(lastUser);
    }
  }, []);
  useEffect(() => {
    fetchDocInfo();
  }, [barbers, docId]);
  useEffect(() => {
    if (barbers) getAvailableSlots();
  }, [barbers]);

  const fetchDocInfo = async () => {
    const barberInfo = barbers.find(barber => barber.barber_id === docId);

    // console.error(fetchMemberId(user));
    setBarbers(barberInfo);
  };


  const bookAppointment = async () => {
    if (!slotTime) {
      alert("กรุณาเลือกช่วงเวลานัดหมาย!");
      return;
    }

    // สร้างวันที่จาก docSlots
    const selectedDate = new Date(docSlots[slotIndex][0].dateTime);
    const [hour, minute] = slotTime.split(':');

    // ตั้งค่าเวลาในวันที่เลือก
    selectedDate.setHours(parseInt(hour), parseInt(minute));

    // เก็บวันและเวลาที่เลือกในรูปแบบที่ต้องการ
    const appointmentDate = `${selectedDate.getFullYear()}-${String(selectedDate.getMonth() + 1).padStart(2, '0')}-${String(selectedDate.getDate()).padStart(2, '0')} ${String(selectedDate.getHours()).padStart(2, '0')}:${String(selectedDate.getMinutes()).padStart(2, '0')}`;

    const requestBody = {
      username: user, // แทนที่ด้วย member ID ที่แท้จริงจาก context หรือ props
      barberId: docId.toString(), // แทนที่ด้วย barber ID ที่แท้จริง
      appointmentDate: appointmentDate, // เก็บวันและเวลาที่เลือก
      serviceType: "Cut" // หรือประเภทบริการที่คุณต้องการส่ง
    };

    try {
      const token = localStorage.getItem("token"); // ดึง token จาก LocalStorage

      const response = await axios.post(`http://localhost:8085/appointment/${docId}`, requestBody, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}` // ใส่ token ใน header
        }
      });

      if (response.status === 200 || response.status === 201) {
        alert("จองนัดหมายเรียบร้อยแล้ว!"+appointmentDate);
        updateSlotStatus(slotIndex, slotTime);
      } else {
        alert("ไม่สามารถจองนัดหมายได้");
      }
    } catch (error) {
      if (error.response && error.response.status === 409) {
        alert("มีการจองซ้ำในวันและเวลานี้สำหรับช่างที่เลือก");
      } else {
        alert("เกิดข้อผิดพลาดระหว่างการจองนัดหมาย");
      }
    }
  };





  const getAvailableSlots = async () => {
    const slots = []; // สร้าง array เพื่อเก็บ slots ทั้งหมด
    let today = new Date();


    for (let i = 0; i < 7; i++) {
      let currentDate = new Date(today);
      currentDate.setDate(today.getDate() + i);
      let endTime = new Date(currentDate);
      endTime.setHours(21, 0, 0, 0);

      if (today.getDate() === currentDate.getDate()) {
        currentDate.setHours(currentDate.getHours() > 10 ? currentDate.getHours() + 1 : 10);
        currentDate.setMinutes(currentDate.getMinutes() > 30 ? 30 : 0);
      } else {
        currentDate.setHours(10, 0);
      }

      let timeSlots = [];
      while (currentDate < endTime) {
        let formattedTime = currentDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

        // ตรวจสอบว่ามีการจองหรือไม่
        const isBooked = bookedAppointments.some(
            appointment =>
                appointment.date === currentDate.toISOString().split('T')[0] &&
                appointment.time === formattedTime
        );

        timeSlots.push({
          dateTime: new Date(currentDate),
          time: formattedTime,
          isBooked: isBooked
        });

        currentDate.setMinutes(currentDate.getMinutes() + 30);
      }

      slots.push(timeSlots);
    }

    setDocSlots(slots);
  };

  const updateSlotStatus = (dayIndex, time) => {
    setDocSlots(prevSlots => {
      const updatedSlots = [...prevSlots];
      updatedSlots[dayIndex] = updatedSlots[dayIndex].map(slot => {
        if (slot.time === time) {
          return { ...slot, isBooked: true }; // กำหนด isBooked เป็น true
        }
        return slot;
      });
      return updatedSlots;
    });
  };



  return barbers && (  /*แก้ชื่อ*/
      <div>
        {/*-------Doctor Details-------*/}
        <div className='flex flex-col sm:flex-row gap-4'>
          <div>
            <img className='bg-primary w-full sm:max-w-72 rounded-lg' src={barbers.image} alt="" />
          </div>
          <div className='flex-1 border border-gray-400 rounded-lg p-8 py-7 bg-white mx-2 sm:mx-0 mt-[-0px] sm:mx-0'>
            <p className='flex items-center gap-2 text-2xl font-medium text-gray-900'>
              {barbers.name}
              <img className='w-5' src={assets.verified_icon} alt="" />
            </p>
            <div className='flex items-center gap-2 text-sm mt-1 text-gray-600'>
              <p>{barbers.gender}</p>
              <button className='py-0.5 px-2 border text-xs rounded-full'>{barbers.experience}</button>
            </div>
            <div>
              <p className='flex items-center gap-1 text-sm font-medium text-gray-900 mt-3'>
                About <img src={assets.info_icon} alt="" />
              </p>
              <p className='text-sm text-gray-500 max-w-[700px] mt-1'>{barbers.about}</p>
            </div>
            <p className='text-gray-500 font-medium mt-4'>
              Appointment fee: <span className='text-gray-600'>{currencySymbol}{"50"}</span>{/*docInfo.fees*/}
            </p>
          </div>
        </div>

        {/*-------Booking Slots-------*/}
        <div className='sm:ml-72 sm:pl-4 mt-4 font-medium text-gray-700'>
          <p>Booking slots</p>
          <div className='flex gap-3 items-center w-full overflow-x-scroll mt-4'>
            {docSlots.length > 0 && docSlots.map((item, index) => (
                <div onClick={() => setSlotIndex(index)} className={`text-center py-6 min-w-16 rounded-full cursor-pointer ${slotIndex === index ? 'bg-primary text-white' : 'border border-gray-200'}`} key={index}>
                  <p>{item[0] && daysOfWeek[item[0].dateTime.getDay()]}</p>
                  <p>{item[0] && item[0].dateTime.getDate()}</p>
                </div>
            ))}
          </div>

          <div className='flex items-center gap-3 w-full overflow-x-scroll mt-4'>
            {docSlots.length > 0 && docSlots[slotIndex]?.map((item, index) => (
                <p
                    onClick={() => !item.isBooked && setSlotTime(item.time)} // ถ้าถูกจองจะไม่สามารถคลิกได้
                    className={`text-sm font-light flex-shrink-0 px-5 py-2 rounded-full cursor-pointer 
        ${item.isBooked ? 'bg-gray-300 text-gray-500 cursor-not-allowed' :
                        item.time === slotTime ? 'bg-primary text-white' : 'text-gray-400 border border-gray-300'}`}
                    key={index}
                >
                  {item.time.toLowerCase()}
                </p>
            ))}
          </div>
          <button className='bg-primary text-white text-sm font-light px-14 py-3 rounded-full my-6' onClick={bookAppointment}>
            Book an Appointment
          </button>

        </div>
        {/*-------Listing Barbers-------*/}
        <TopDoctors />
      </div>
  );
};

export default Appointment;
