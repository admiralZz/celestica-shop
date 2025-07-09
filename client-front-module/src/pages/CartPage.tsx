import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';

const CartPage: React.FC = () => {
  const { items, removeFromCart, updateQuantity, getTotalPrice, clearCart } = useCart();

  if (items.length === 0) {
    return (
      <div className="container mx-auto px-4 py-8 text-center">
        <h1 className="text-2xl font-bold mb-6 text-gray-800">Корзина</h1>
        <p className="text-lg text-gray-600 mb-4">Ваша корзина пуста</p>
        <Link to="/" className="text-blue-500 hover:underline">
          Перейти в каталог
        </Link>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6 text-gray-800">Корзина</h1>
      
      <div className="bg-white rounded-lg shadow-lg p-6">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b">
                <th className="text-left py-4 px-2">Товар</th>
                <th className="text-center py-4 px-2">Цена</th>
                <th className="text-center py-4 px-2">Количество</th>
                <th className="text-center py-4 px-2">Сумма</th>
                <th className="text-right py-4 px-2">Действия</th>
              </tr>
            </thead>
            <tbody>
              {items.map(item => (
                <tr key={item.product.id} className="border-b">
                  <td className="py-4 px-2">
                    <div className="flex items-center">
                      <img 
                        src={item.product.imageUrl ?? '/static/placeholder.jpg'} 
                        alt={item.product.name}
                        className="w-16 h-16 object-cover rounded mr-4"
                      />
                      <Link to={`/product/${item.product.id}`} className="hover:text-blue-500">
                        {item.product.name}
                      </Link>
                    </div>
                  </td>
                  <td className="text-center py-4 px-2">
                    {item.product.price.toLocaleString()} ₽
                  </td>
                  <td className="text-center py-4 px-2">
                    <div className="flex items-center justify-center">
                      <button 
                        onClick={() => updateQuantity(item.product.id, item.quantity - 1)}
                        className="bg-gray-200 px-2 py-1 rounded-l"
                        aria-label="Уменьшить количество"
                      >
                        -
                      </button>
                      <span className="px-4 py-1 bg-gray-100">
                        {item.quantity}
                      </span>
                      <button 
                        onClick={() => updateQuantity(item.product.id, item.quantity + 1)}
                        className="bg-gray-200 px-2 py-1 rounded-r"
                        aria-label="Увеличить количество"
                      >
                        +
                      </button>
                    </div>
                  </td>
                  <td className="text-center py-4 px-2">
                    {(item.product.price * item.quantity).toLocaleString()} ₽
                  </td>
                  <td className="text-right py-4 px-2">
                    <button 
                      onClick={() => removeFromCart(item.product.id)}
                      className="text-red-500 hover:text-red-700"
                      aria-label="Удалить товар"
                    >
                      Удалить
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        
        <div className="mt-6 flex flex-col md:flex-row justify-between items-start md:items-center">
          <button 
            onClick={clearCart}
            className="text-red-500 hover:text-red-700 mb-4 md:mb-0"
          >
            Очистить корзину
          </button>
          
          <div className="text-right">
            <p className="text-lg mb-2">
              Итого: <span className="font-bold">{getTotalPrice().toLocaleString()} ₽</span>
            </p>
            <Link to="/checkout" className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-6 rounded inline-block text-center">
              Оформить заказ
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartPage; 