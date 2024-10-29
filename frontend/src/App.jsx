// import React, { useEffect, useState } from 'react';
// import { Route, Routes } from 'react-router-dom';
// import Home from './pages/Home';
// import Doctors from './pages/Doctors';
// import Contact from './pages/Contact';
// import Auth from './pages/Auth';
// import About from './pages/About';
// import MyProfile from './pages/MyProfile';
// import MyAppointments from './pages/MyAppointments';
// import Appointment from './pages/Appointment';
// import Navbar from './components/Navbar';
// import Footer from './components/Footer';
// import './index.css';
// import axios from 'axios';
//
// const App = () => {
//     const [data, setData] = useState('');
//
//     useEffect(() => {
//         const fetchData = async () => {
//             try {
//                 const response = await axios.get('/http://localhost:8085', { validateStatus: false });
//                 const contentType = response.headers['content-type'];
//                 const status = response.status;
//
//                 // ตรวจสอบว่า Content-Type เป็น JSON และสถานะเป็น 200
//                 if (contentType && contentType.includes('application/json') && status === 200) {
//                     setData(response.data.message || response.data);
//                 } else {
//                     console.error('Non-JSON or Redirect response received, skipping setting data.');
//                 }
//             } catch (error) {
//                 console.error('Error fetching data:', error);
//             }
//         };
//         fetchData();
//     }, []);
//
//     return (
//         <div className='mx-4 sm:mx-[10%]'>
//             <Navbar />
//             <h1>{data}</h1>
//             <Routes>
//                 <Route path='/' element={<Home />} />
//                 <Route path='/login' element={<Auth />} />
//                 <Route path='/SignUp' element={<Auth />} />
//                 <Route path='/doctors' element={<Doctors />} />
//                 <Route path='/doctors/:speciality' element={<Doctors />} />
//                 <Route path='/doctors/:topdoctors' element={<Doctors />} />
//                 <Route path='/about' element={<About />} />
//                 <Route path='/contact' element={<Contact />} />
//                 <Route path='/my-profile' element={<MyProfile />} />
//                 <Route path='/my-appointments' element={<MyAppointments />} />
//                 <Route path='/appointment/:docId' element={<Appointment />} />
//             </Routes>
//             <Footer />
//         </div>
//     );
// };
//
// export default App;

import React, { useEffect, useState } from 'react'; // เพิ่ม useEffect และ useState
import { Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
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
