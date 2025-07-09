import React, { useState, useEffect } from 'react';
import { useCart } from '../context/CartContext';
import { Link } from 'react-router-dom';
import { checkOrder, createOrder } from '../api/client';
import { ChosenOrderItem, OrderItem, CreateOrder, OrderBody } from '../api/dto';

const CheckoutPage: React.FC = () => {
  const { items, clearCart } = useCart();
  const [validatedItems, setValidatedItems] = useState<OrderItem[]>([]);
  const [validatedTotal, setValidatedTotal] = useState<number>(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [orderDetails, setOrderDetails] = useState<OrderBody | null>(null);
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [address, setAddress] = useState('');
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const fetchValidated = async () => {
      setLoading(true);
      setError(null);
      try {
        const productsForCheck: ChosenOrderItem[] = items.map(item => ({
          productId: item.product.id,
          quantity: item.quantity
        }));
        const data = await checkOrder(productsForCheck);
        setValidatedItems(data.items);
        setValidatedTotal(data.total);
      } catch (err: any) {
        setError(err.message || 'Ошибка проверки заказа');
      } finally {
        setLoading(false);
      }
    };
    fetchValidated();
    // eslint-disable-next-line
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      const order: CreateOrder = {
        items: validatedItems.map(item => ({
          productId: item.product.id,
          quantity: item.quantity
        })),
        email: name, // если нужен email, иначе поменяй на отдельное поле
        phone,
        address
      };
      const response = await createOrder(order);
      setOrderDetails(response);
      clearCart();
    } catch (err: any) {
      setError(err.message || 'Ошибка оформления заказа');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8 text-center">
        <p className="text-lg text-blue-500">Проверяем заказ...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container mx-auto px-4 py-8 text-center">
        <p className="text-lg text-red-500">{error}</p>
        <Link to="/cart" className="text-blue-500 hover:underline">Вернуться в корзину</Link>
      </div>
    );
  }

  if (orderDetails) {
    return (
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-2xl font-bold mb-6 text-gray-800">Заказ успешно оформлен!</h1>
        <div className="bg-white rounded-lg shadow-lg p-6 mb-8">
          <h2 className="text-xl font-semibold mb-4">Детали заказа</h2>
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead>
                <tr>
                  <th className="py-2 px-2 font-semibold text-gray-700">Позиция</th>
                  <th className="py-2 px-2 font-semibold text-gray-700 text-center">Цена</th>
                  <th className="py-2 px-2 font-semibold text-gray-700 text-center">Количество</th>
                  <th className="py-2 px-2 font-semibold text-gray-700 text-right">Итого</th>
                </tr>
              </thead>
              <tbody>
                {orderDetails.items.map(item => (
                  <tr key={item.product.id} className="border-b last:border-b-0">
                    <td className="py-2 px-2">{item.product.name}</td>
                    <td className="py-2 px-2 text-center text-gray-500">{item.product.price.toLocaleString()} ₽/шт</td>
                    <td className="py-2 px-2 text-center">{item.quantity}</td>
                    <td className="py-2 px-2 text-right font-semibold">{item.price.toLocaleString()} ₽</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <div className="text-right font-bold text-lg mt-4">
            Итоговая сумма: {orderDetails.total.toLocaleString()} ₽
          </div>
          <div className="mt-6">
            <div className="mb-2">Email: <span className="font-medium">{orderDetails.email}</span></div>
            <div className="mb-2">Телефон: <span className="font-medium">{orderDetails.phone}</span></div>
            <div className="mb-2">Адрес: <span className="font-medium">{orderDetails.address}</span></div>
          </div>
        </div>
        <Link to="/" className="text-blue-500 hover:underline">Вернуться в каталог</Link>
      </div>
    );
  }

  if (validatedItems.length === 0) {
    return (
      <div className="container mx-auto px-4 py-8 text-center">
        <h1 className="text-2xl font-bold mb-6 text-gray-800">Оформление заказа</h1>
        <p className="text-lg text-gray-600 mb-4">Ваша корзина пуста</p>
        <Link to="/cart" className="text-blue-500 hover:underline">Вернуться в корзину</Link>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6 text-gray-800">Оформление заказа</h1>
      <div className="bg-white rounded-lg shadow-lg p-6 mb-8">
        <h2 className="text-xl font-semibold mb-4">Ваш заказ</h2>
        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr>
                <th className="py-2 px-2 font-semibold text-gray-700">Позиция</th>
                <th className="py-2 px-2 font-semibold text-gray-700 text-center">Цена</th>
                <th className="py-2 px-2 font-semibold text-gray-700 text-center">Количество</th>
                <th className="py-2 px-2 font-semibold text-gray-700 text-right">Итого</th>
              </tr>
            </thead>
            <tbody>
              {validatedItems.map(item => (
                <tr key={item.product.id} className="border-b last:border-b-0">
                  <td className="py-2 px-2">{item.product.name}</td>
                  <td className="py-2 px-2 text-center text-gray-500">{item.product.price.toLocaleString()} ₽/шт</td>
                  <td className="py-2 px-2 text-center">{item.quantity}</td>
                  <td className="py-2 px-2 text-right font-semibold">{item.price.toLocaleString()} ₽</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="text-right font-bold text-lg mt-4">
          Итоговая сумма: {validatedTotal.toLocaleString()} ₽
        </div>
      </div>
      <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow-lg p-6">
        <div className="mb-4">
          <label className="block mb-2 text-gray-700 font-medium" htmlFor="name">Email</label>
          <input
            id="name"
            type="email"
            className="w-full border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={name}
            onChange={e => setName(e.target.value)}
            required
            autoComplete="email"
          />
        </div>
        <div className="mb-4">
          <label className="block mb-2 text-gray-700 font-medium" htmlFor="phone">Телефон</label>
          <input
            id="phone"
            type="tel"
            className="w-full border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={phone}
            onChange={e => setPhone(e.target.value)}
            required
            autoComplete="tel"
          />
        </div>
        <div className="mb-6">
          <label className="block mb-2 text-gray-700 font-medium" htmlFor="address">Адрес доставки</label>
          <input
            id="address"
            type="text"
            className="w-full border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={address}
            onChange={e => setAddress(e.target.value)}
            required
            autoComplete="street-address"
          />
        </div>
        <button
          type="submit"
          className="w-full py-3 px-6 rounded-lg text-white font-medium bg-blue-500 hover:bg-blue-600 transition-colors"
          disabled={submitting || validatedItems.length === 0}
        >
          {submitting ? 'Оформляем...' : 'Оформить заказ'}
        </button>
      </form>
    </div>
  );
};

export default CheckoutPage; 