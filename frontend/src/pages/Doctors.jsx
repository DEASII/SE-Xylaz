import React, { useContext, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { AppContext } from '../context/AppContext';

const Doctors = () => {
  const { speciality } = useParams();
  const [filterDoc, setFilterDoc] = useState([]);
  const [showFilter, setShowFilter] = useState(false);
  const [gender, setGender] = useState(null); // State สำหรับเก็บ gender ที่เลือก
  const navigate = useNavigate();
  const { doctors } = useContext(AppContext);

  // ฟังก์ชันกรองข้อมูลแพทย์ตาม speciality และ gender
  useEffect(() => {
    let filteredDoctors = doctors;

    // กรองตาม speciality
    if (speciality) {
      filteredDoctors = filteredDoctors.filter(doc => doc.speciality === speciality);
    }

    // กรองตาม gender
    if (gender) {
      filteredDoctors = filteredDoctors.filter(doc => doc.gender === gender);
    }

    setFilterDoc(filteredDoctors);
  }, [doctors, speciality, gender]); // Dependency array รวม gender เพื่อ update เมื่อค่าเปลี่ยน

  return (
      <div className="p-5">
        <p className="text-gray-600">Browse through the expert barbers.</p>

        <div className="flex flex-col sm:flex-row items-start gap-5 mt-5">
          {/* ปุ่มสำหรับแสดง/ซ่อนตัวกรอง /}
        <button
          className={py-1 px-3 border rounded text-sm transition-all sm:hidden ${showFilter ? 'bg-primary text-white' : ''}}
          onClick={() => setShowFilter(prev => !prev)}
        >
          Filters
        </button>

        {/ ตัวกรองเพศ /}
        <div className={flex-col gap-4 text-sm text-gray-600 ${showFilter ? 'flex' : 'hidden sm:flex'}}>
          <p
            onClick={() => setGender(gender === 'Male' ? null : 'Male')}
            className={w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${gender === "Male" ? "bg-indigo-100 text-black" : ""}}
          >
            Male
          </p>
          <p
            onClick={() => setGender(gender === 'Female' ? null : 'Female')}
            className={w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${gender === "Female" ? "bg-indigo-100 text-black" : ""}}
          >
            Female
          </p>
        </div>

        {/ แสดงรายชื่อแพทย์ */}
          <div className="w-full grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4 pt-5 gap-y-6">
            {
              filterDoc.map((item, index) => (
                  <div
                      onClick={() => navigate(`/appointment/${item._id}`)}
                      className="border border-blue-200 rounded-xl overflow-hidden cursor-pointer transform hover:scale-105 transition-all duration-300"
                      key={index}
                  >
                    <img className="w-full h-60 object-cover bg-blue-50" src={item.image} alt={item.name} />
                    <div className="p-4">
                      <div className="flex items-center gap-2 text-sm text-center text-green-500">
                        <p className="w-2 h-2 bg-green-500 rounded-full"></p>
                        <p>Available</p>
                      </div>
                      <p className="text-gray-900 text-lg font-medium">{item.name}</p>
                      <p className="text-gray-600 text-sm">{item.gender}</p>
                    </div>
                  </div>
              ))
            }
          </div>
        </div>
      </div>
  );
};

export default Doctors;