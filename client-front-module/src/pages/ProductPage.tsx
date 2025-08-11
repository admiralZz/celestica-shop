import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getItemById } from '../api/client';
import { Product } from '../api/dto';
import { useCart } from '../context/CartContext';
import { useTranslation } from 'react-i18next';

const ProductPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const { addToCart } = useCart();
  const { t } = useTranslation();

  useEffect(() => {
    const fetchProduct = async () => {
      if (!id) return;
      
      try {
        setLoading(true);
        const productData = await getItemById(parseInt(id));
        setProduct(productData);
        setError(null);
      } catch (err) {
        console.error('Error fetching product:', err);
        setError(t('product.loadError'));
      } finally {
        setLoading(false);
      }
    };

    fetchProduct();
  }, [id, t]);

  const handleAddToCart = () => {
    if (product) {
      addToCart(product);
    }
  };

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8 text-center">
        <p className="text-lg">{t('product.loading')}</p>
      </div>
    );
  }

  if (error || !product) {
    return (
      <div className="container mx-auto px-4 py-8 text-center">
        <p className="text-lg text-red-500">{error || t('product.notFound')}</p>
        <Link to="/" className="text-blue-500 hover:underline mt-4 inline-block">
          {t('product.backToCatalog')}
        </Link>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <Link to="/" className="text-blue-500 hover:underline mb-6 inline-block">
        {t('product.backToCatalog')}
      </Link>
      
      <div className="bg-white rounded-lg shadow-lg p-6 mt-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          {/* Product Image */}
          <div className="overflow-hidden rounded-lg">
            <img 
              src={product.imageUrl ?? '/static/placeholder.jpg'} 
              alt={product.name}
              className="w-full h-auto object-cover"
            />
          </div>
          
          {/* Product Info */}
          <div>
            <h1 className="text-2xl font-bold text-gray-800 mb-4">{product.name}</h1>
            <p className="text-gray-600 mb-6">{product.description}</p>
            
            <div className="mb-6">
              <span className="text-3xl font-bold text-blue-600">{product.price.toLocaleString()} ₽</span>
              {product.stockQuantity > 0 ? (
                <span className="ml-3 text-green-500">{t('product.inStock')}</span>
              ) : (
                <span className="ml-3 text-red-500">{t('product.outOfStock')}</span>
              )}
            </div>
            
            <div className="mb-6">
              <p className="text-gray-700 mb-2">{t('product.category')}: <span className="font-medium">{product.categoryName}</span></p>
            </div>
            
            <button 
              onClick={handleAddToCart}
              disabled={product.stockQuantity <= 0}
              className={`w-full py-3 px-6 rounded-lg text-white font-medium ${
                product.stockQuantity > 0 
                  ? 'bg-blue-500 hover:bg-blue-600' 
                  : 'bg-gray-400 cursor-not-allowed'
              } transition-colors`}
            >
              {t('product.addToCart')}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductPage; 