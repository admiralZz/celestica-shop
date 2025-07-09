import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { getItemById } from '../api/client';

interface ProductCardProps {
  id: number;
  name: string;
  price: number;
  imageUrl?: string;
}

const ProductCard: React.FC<ProductCardProps> = ({ id, name, price, imageUrl }) => {
  const { addToCart } = useCart();

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    
    try {
      const product = await getItemById(id);
      addToCart(product);
    } catch (err) {
      console.error('Error adding product to cart:', err);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden transition-transform hover:scale-105">
      <Link to={`/product/${id}`} className="block">
        <div className="h-48 overflow-hidden">
          <img 
            src={imageUrl ?? '/static/placeholder.jpg'}
            alt={name} 
            className="w-full h-full object-cover"
          />
        </div>
        <div className="p-4">
          <h3 className="text-lg font-semibold text-gray-800 mb-2">{name}</h3>
          <div className="flex justify-between items-center">
            <span className="text-blue-600 font-bold">{price.toLocaleString()} ₽</span>
          </div>
        </div>
      </Link>
      <div className="px-4 pb-4 -mt-2">
        <button 
          onClick={handleAddToCart}
          className="w-full bg-blue-500 hover:bg-blue-600 text-white px-3 py-2 rounded text-sm transition-colors"
          aria-label="Добавить в корзину"
        >
          В корзину
        </button>
      </div>
    </div>
  );
};

export default ProductCard; 