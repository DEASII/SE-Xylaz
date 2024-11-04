import React, { useEffect, useState } from 'react'; // เพิ่ม useEffect และ useState
import { Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import Payment from './pages/Payment';
import Doctors from './pages/Doctors';
import Contact from './pages/Contact';
import Auth from './pages/Auth'; // ตรวจสอบ path ให้ถูกต้อง
import About from './pages/About';
import MyProfile from './pages/MyProfile';
import MyAppointments from './pages/MyAppointments';
import Appointment from './pages/Appointment';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import './index.css'; // ชื่อไฟล์ CSS ของคุณ
import axios from 'axios'; // เพิ่ม axios

const App = () => {
    const [data, setData] = useState(''); // สถานะสำหรับข้อมูลที่ดึงมา

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8085'); // URL API ของคุณ
                // setData(response.data); // อัพเดตสถานะด้วยข้อมูลที่ได้
            } catch (error) {
                console.error('Error fetching data:', error); // แสดงข้อผิดพลาดในคอนโซล
            }
        };
        fetchData(); // เรียกฟังก์ชันเพื่อดึงข้อมูล
    }, []); // ใช้ [] เพื่อให้ useEffect ทำงานแค่ครั้งเดียวเมื่อคอมโพเนนต์โหลด

    return (
        <div className='mx-4 sm:mx-[10%]'>
            <Navbar />
            <h1>{data}</h1> {/* แสดงข้อมูลที่ดึงมา */}
            <Routes>
                <Route path='/signin' element={<Auth />} /> {/* เปลี่ยนจาก <Home /> เป็น <Login /> */}
                <Route path='/SignUp' element={<Auth />} /> {/* เปลี่ยนจาก <Home /> เป็น <Login /> */}
                <Route path='/' element={<Home />} /> {/* เพิ่มเส้นทางสำหรับหน้า Home */}
                <Route path='/doctors' element={<Doctors />} />
                <Route path='/doctors/:speciality' element={<Doctors />} />
                <Route path='/doctors/:topdoctors' element={<Doctors />} />
                <Route path='/payment' element={<Payment />} />
                <Route path='/about' element={<About />} />
                <Route path='/contact' element={<Contact />} />
                <Route path='/my-profile' element={<MyProfile />} />
                <Route path='/my-appointments' element={<MyAppointments />} />
                <Route path='/appointment/:docId' element={<Appointment />} />
            </Routes>
            <Footer />
        </div>
    );
};

export default App;
