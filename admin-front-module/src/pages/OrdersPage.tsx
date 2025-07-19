import React, { useEffect, useState } from 'react';
import { getOrders } from '../api/client';
import { OrderBody } from '../api/dto';

const OrdersPage: React.FC = () => {
  const [orders, setOrders] = useState<OrderBody[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    getOrders()
      .then(res => setOrders(res.orders))
      .catch(() => setError('Ошибка загрузки заказов'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="w-full max-w-screen-2xl mx-auto flex bg-white rounded shadow min-h-[500px] px-4">
      <section className="flex-1 p-8">
        <h2 className="text-xl font-bold mb-4">Заказы</h2>
        {loading ? (
          <div className="text-gray-500">Загрузка заказов...</div>
        ) : error ? (
          <div className="text-red-500">{error}</div>
        ) : (
          <table className="min-w-full border text-sm rounded overflow-hidden">
            <thead>
              <tr className="bg-gray-100">
                <th className="px-4 py-2 text-left font-semibold">ID</th>
                <th className="px-4 py-2 text-left font-semibold">Email</th>
                <th className="px-4 py-2 text-left font-semibold">Телефон</th>
                <th className="px-4 py-2 text-left font-semibold">Адрес</th>
                <th className="px-4 py-2 text-left font-semibold">Дата/время</th>
                <th className="px-4 py-2 text-left font-semibold">Сумма</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order, idx) => (
                <tr key={idx} className="border-t hover:bg-blue-50">
                  <td className="px-4 py-2">{idx + 1}</td>
                  <td className="px-4 py-2">{order.email}</td>
                  <td className="px-4 py-2">{order.phone}</td>
                  <td className="px-4 py-2">{order.address}</td>
                  <td className="px-4 py-2">{order.datetime.toLocaleString()}</td>
                  <td className="px-4 py-2">{order.total}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </div>
  );
};

export default OrdersPage; 