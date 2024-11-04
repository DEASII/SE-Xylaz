import React from 'react';

const Payment = () => {
    return (
        <div className=" flex flex-col items-center justify-center h-screen ">
            <div className="w-80 h-80 bg-blue-500 rounded-lg shadow-lg flex items-center justify-center mb-4">
                <h2 className="text-white text-2xl font-bold">Payment Area</h2>
            </div>
            <button className="px-6 py-2 text-white bg-green-600 rounded-lg shadow transition duration-300 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-400 focus:ring-opacity-50">
                Finish
            </button>
        </div>
    );
};

export default Payment;
