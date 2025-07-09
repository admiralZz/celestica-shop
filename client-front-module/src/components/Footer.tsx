import React from 'react';
import Logo from './Logo';

const Footer: React.FC = () => {
  return (
    <footer className="bg-gray-800 text-white py-8">
      <div className="container mx-auto px-4">
        <div className="flex flex-wrap justify-between">
          <div className="w-full md:w-1/3 mb-6 md:mb-0">
            <Logo className="text-white mb-4" />
            <p className="text-gray-300">
              Ваш надежный магазин для покупки качественных товаров.
            </p>
          </div>
          
          <div className="w-full md:w-1/3 mb-6 md:mb-0">
            <h3 className="text-lg font-semibold mb-4">Ссылки</h3>
            <ul className="space-y-2">
              <li><a href="#" className="hover:text-blue-300 transition-colors">Главная</a></li>
              <li><a href="#" className="hover:text-blue-300 transition-colors">Каталог</a></li>
              <li><a href="#" className="hover:text-blue-300 transition-colors">О нас</a></li>
              <li><a href="#" className="hover:text-blue-300 transition-colors">Контакты</a></li>
            </ul>
          </div>
          
          <div className="w-full md:w-1/3">
            <h3 className="text-lg font-semibold mb-4">Контакты</h3>
            <address className="not-italic text-gray-300">
              <p>Адрес: ул. Примерная, 123</p>
              <p>Телефон: +7 (123) 456-78-90</p>
              <p>Email: sales@celestica.com</p>
            </address>
          </div>
        </div>
        
        <div className="border-t border-gray-700 mt-8 pt-6 text-center text-gray-400">
          <p>&copy; {new Date().getFullYear()} Celestica Shop. Все права защищены.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer; 