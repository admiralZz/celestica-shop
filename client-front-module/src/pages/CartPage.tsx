import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { useTranslation } from 'react-i18next';

const CartPage: React.FC = () => {
  const { items, removeFromCart, updateQuantity, getTotalPrice, clearCart } = useCart();
  const { t } = useTranslation();

  if (items.length === 0) {
    return (
      <div className="container mx-auto px-4 py-8 text-center">
        <h1 className="text-2xl font-bold mb-6 text-gray-800">{t('cart.title')}</h1>
        <p className="text-lg text-gray-600 mb-4">{t('cart.empty')}</p>
        <Link to="/" className="text-blue-500 hover:underline">
          {t('cart.goCatalog')}
        </Link>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6 text-gray-800">{t('cart.title')}</h1>
      
      <div className="bg-white rounded-lg shadow-lg p-6">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b">
                <th className="py-4 px-2"></th> {/* image */}
                <th className="text-left py-4 px-2">{t('cart.product')}</th>
                <th className="text-center py-4 px-2">{t('cart.price')}</th>
                <th className="text-center py-4 px-2">{t('cart.quantity')}</th>
                <th className="text-center py-4 px-2">{t('cart.sum')}</th>
                <th className="text-right py-4 px-2">{t('cart.actions')}</th>
              </tr>
            </thead>
            <tbody>
              {items.map(item => (
                <tr key={item.product.id} className="border-b">
                  <td className="py-4 px-2 min-w-[64px]">
                    <img 
                      src={item.product.imageUrl ?? '/static/placeholder.jpg'} 
                      alt={item.product.name}
                      className="w-16 h-16 object-cover rounded"
                    />
                  </td>
                  <td className="py-4 px-2">
                    <Link to={`/product/${item.product.id}`} className="hover:text-blue-500">
                      {item.product.name}
                    </Link>
                  </td>
                  <td className="text-center py-4 px-2">
                    {item.product.price.toLocaleString()} ₽
                  </td>
                  <td className="text-center py-4 px-2">
                    <div className="flex items-center justify-center">
                      <button 
                        onClick={() => updateQuantity(item.product.id, item.quantity - 1)}
                        className="bg-gray-200 px-2 py-1 rounded-l"
                        aria-label={t('cart.decrease')}
                      >
                        -
                      </button>
                      <span className="px-4 py-1 bg-gray-100">
                        {item.quantity}
                      </span>
                      <button 
                        onClick={() => updateQuantity(item.product.id, item.quantity + 1)}
                        className="bg-gray-200 px-2 py-1 rounded-r"
                        aria-label={t('cart.increase')}
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
                      {t('cart.remove')}
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
            {t('cart.clear')}
          </button>
          
          <div className="text-right">
            <p className="text-lg mb-2">
              {t('cart.total')}: <span className="font-bold">{getTotalPrice().toLocaleString()} ₽</span>
            </p>
            <Link to="/checkout" className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-6 rounded inline-block text-center">
              {t('cart.checkout')}
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartPage; 