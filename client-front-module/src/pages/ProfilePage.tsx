import React, { useState, useEffect } from 'react';
import { getMyOrders } from '../api/client';
import type { OrderBody } from '../api/dto';
import { Link } from 'react-router-dom';

const TABS = [
  { key: 'orders', label: 'Мои заказы' },
  { key: 'settings', label: 'Настройки' },
];

const OrderPanel: React.FC = () => {
  const [orders, setOrders] = useState<OrderBody[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchOrders = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await getMyOrders();
        setOrders(res.orders);
      } catch (err: any) {
        setError(err.message || 'Ошибка загрузки заказов');
      } finally {
        setLoading(false);
      }
    };
    fetchOrders();
  }, []);

  if (loading) return <div className="text-blue-500">Загрузка заказов...</div>;
  if (error) return <div className="text-red-500">{error}</div>;
  if (!orders.length) return <div className="text-gray-500">У вас пока нет заказов.</div>;

  return (
    <div className="flex flex-col gap-6">
      {orders.map((order, idx) => (
        <div key={idx} className="border rounded-lg p-4 bg-gray-50">
          <div className="mb-2 flex flex-wrap gap-4 text-sm text-gray-700">
            <span>Email: <span className="font-medium">{order.email}</span></span>
            <span>Телефон: <span className="font-medium">{order.phone}</span></span>
            <span>Адрес: <span className="font-medium">{order.address}</span></span>
            <span>Сумма: <span className="font-bold text-blue-600">{order.total.toLocaleString()} ₽</span></span>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead>
                <tr className="border-b">
                  <th className="py-2 px-2 font-semibold">Товар</th>
                  <th className="py-2 px-2 font-semibold text-center">Цена</th>
                  <th className="py-2 px-2 font-semibold text-center">Кол-во</th>
                  <th className="py-2 px-2 font-semibold text-right">Итого</th>
                </tr>
              </thead>
              <tbody>
                {order.items.map((item, i) => (
                  <tr key={i} className="border-b last:border-b-0">
                    <td className="py-2 px-2">
                      <Link
                        to={`/product/${item.product.id}`}
                        className="hover:text-blue-500 underline"
                        tabIndex={0}
                        aria-label={`Открыть страницу товара ${item.product.name}`}
                      >
                        {item.product.name}
                      </Link>
                    </td>
                    <td className="py-2 px-2 text-center">{item.product.price.toLocaleString()} ₽</td>
                    <td className="py-2 px-2 text-center">{item.quantity}</td>
                    <td className="py-2 px-2 text-right font-semibold">{item.price.toLocaleString()} ₽</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ))}
    </div>
  );
};

const ProfilePage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'orders' | 'settings'>('orders');

  return (
    <div className="container mx-auto p-8 flex gap-8">
      <aside className="w-56">
        <nav className="flex flex-col gap-2">
          {TABS.map(tab => (
            <button
              key={tab.key}
              className={`text-left px-4 py-2 rounded font-medium transition-colors focus:outline-none ${activeTab === tab.key ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-800 hover:bg-blue-100'}`}
              onClick={() => setActiveTab(tab.key as 'orders' | 'settings')}
              tabIndex={0}
              aria-current={activeTab === tab.key}
            >
              {tab.label}
            </button>
          ))}
        </nav>
      </aside>
      <section className="flex-1 bg-white rounded shadow p-6 min-h-[300px]">
        {activeTab === 'orders' && <OrderPanel />}
        {activeTab === 'settings' && <div>Здесь будут настройки профиля</div>}
      </section>
    </div>
  );
};

export default ProfilePage; 