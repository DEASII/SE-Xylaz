import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Auth = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [state, setState] = useState('signin'); // เริ่มต้นที่หน้า Login
  const navigate = useNavigate(); // ใช้ useNavigate แทน history

  const handleLogin = async (e) => {
    e.preventDefault();

    // ตรวจสอบว่ามีการกรอก username และ password ครบถ้วนหรือไม่
    if (!username || !password) {
      alert("Please enter both username and password.");
      return; // หยุดการทำงานถ้าไม่ได้กรอกข้อมูลครบ
    }

    try {
      const response = await axios.post('http://localhost:8085/signin', {
        username,
        password,
      });
      if (response.status === 200) {
        setState("login"); // เปลี่ยนสถานะกลับไปที่ Login หลังจากสมัครสมาชิก
        navigate("/");
      }
    } catch (error) {
      // แสดงข้อความจากเซิร์ฟเวอร์ถ้าหากเกิดข้อผิดพลาด
      const errorMessage = error.response?.data || "Login failed. Please try again.";
      alert(errorMessage);
      // อย่าทำนำทางไปยังหน้า About ถ้าเข้าสู่ระบบไม่สำเร็จ
    }
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    if (password.length < 8) {
      alert("Password must be at least 8 characters long.");
      return; // หยุดการทำงานถ้ารหัสผ่านน้อยกว่า 8 ตัว
    }

    try {
      const response = await axios.post('http://localhost:8085/signup', {
        username,
        password,
        name,
      });

      // ตรวจสอบสถานะ HTTP 200
      if (response.status === 200) {
        alert("Signup successful"); // แสดงข้อความสำเร็จ
        navigate("/login");
      }
    } catch (error) {
      // ตรวจสอบข้อความผิดพลาดที่ได้รับจากเซิร์ฟเวอร์
      const errorMessage = error.response?.data || "Signup failed. Please try again.";
      alert(errorMessage); // แสดงข้อความผิดพลาด
    }
  };

  return (
      <form
          className="min-h-[80vh] flex items-center"
          onSubmit={state === "Sign Up" ? handleSignup : handleLogin}
      >
        <div className="flex flex-col gap-3 m-auto items-start p-8 min-w-[340px] sm:min-w-96 border rounded-xl text-zinc-600 text-sm shadow-lg">
          <p className="text-2xl font-semibold">
            {state === "Sign Up" ? "Create Account" : "Login"}
          </p>
          <p>Please {state === "Sign Up" ? "sign up" : "log in"} to book appointment</p>

          {/* รับ Name แทน Email */}
          {state === "Sign Up" && (
              <div className="w-full">
                <p>Name</p>
                <input
                    className="border border-zinc-300 rounded w-full p-2 mt-1"
                    type="text"
                    onChange={(e) => setName(e.target.value)}
                    value={name}
                    required
                />
              </div>
          )}

          <div className="w-full">
            <p>Username</p>
            <input
                className="border border-zinc-300 rounded w-full p-2 mt-1"
                type="text"
                onChange={(e) => setUsername(e.target.value)}
                value={username}
                required
            />
          </div>

          <div className="w-full">
            <p>Password</p>
            <input
                className="border border-zinc-300 rounded w-full p-2 mt-1"
                type="password"
                onChange={(e) => setPassword(e.target.value)}
                value={password}
                required
            />
          </div>

          <button className="bg-primary text-white w-full py-2 rounded-md text-base">
            {state === "Sign Up" ? "Create account" : "Login"}
          </button>

          {state === "Sign Up" ? (
              <p>
                Already have an account?
                <span
                    onClick={() => {
                      setState("Login");
                      navigate("/signin");
                    }}
                    className="text-primary underline cursor-pointer"
                >
              {" "}Login here
            </span>
              </p>
          ) : (
              <p>
                Create a new account?
                <span
                    onClick={() => {
                      setState("Sign Up");
                      navigate("/signup");
                    }}
                    className="text-primary underline cursor-pointer"
                >
              {" "}click here
            </span>
              </p>
          )}
        </div>
      </form>
  );
};

export default Auth;
