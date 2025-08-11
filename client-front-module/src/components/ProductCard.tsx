import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { getItemById } from '../api/client';
import { useTranslation } from 'react-i18next';

interface ProductCardProps {
  id: number;
  name: string;
  price: number;
  imageUrl?: string;
}

const ProductCard: React.FC<ProductCardProps> = ({ id, name, price, imageUrl }) => {
  const { addToCart } = useCart();
  const [showAdded, setShowAdded] = React.useState(false);
  const { t } = useTranslation();

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    
    try {
      const product = await getItemById(id);
      addToCart(product);
      setShowAdded(true);
      setTimeout(() => setShowAdded(false), 1500);
    } catch (err) {
      console.error('Error adding product to cart:', err);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-visible transition-transform hover:scale-105 group">
      <Link to={`/product/${id}`} className="block">
        <div className="aspect-[4/3] w-full overflow-visible relative z-0">
          <img 
            src={imageUrl ?? '/static/placeholder.jpg'}
            alt={name} 
            className="w-full h-full object-contain"
          />
        </div>
        <div className="p-4">
          <h3 className="text-lg font-semibold text-gray-800 mb-2">{name}</h3>
          <div className="flex justify-between items-center">
            <span className="text-blue-600 font-bold">{price.toLocaleString()} ₽</span>
          </div>
        </div>
      </Link>
      <div className="px-4 pb-4 -mt-2 flex justify-end">
        <button 
          onClick={handleAddToCart}
          className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded text-sm transition-colors relative min-w-[120px] flex items-center gap-2"
          aria-label={t('product.addToCart')}
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-4 h-4">
            <path strokeLinecap="round" strokeLinejoin="round" d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437m0 0L7.5 14.25A2.25 2.25 0 009.664 16.5h4.672a2.25 2.25 0 002.164-2.25l1.394-8.978m-12.25 0h12.25m-12.25 0L4.5 6.75m0 0h15m-1.5 12a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zm-9 0a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0z" />
          </svg>
          {t('product.addToCart')}
          {showAdded && (
            <span className="absolute left-1/2 -translate-x-1/2 -top-8 bg-green-500 text-white px-3 py-1 rounded shadow text-xs">
              {t('product.added')}
            </span>
          )}
        </button>
      </div>
    </div>
  );
};

export default ProductCard; 