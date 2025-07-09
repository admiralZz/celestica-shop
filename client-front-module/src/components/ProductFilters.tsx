import React from 'react';

interface ProductFiltersProps {
  onSearchChange: (value: string) => void;
  onSortChange: (value: string) => void;
}

const ProductFilters: React.FC<ProductFiltersProps> = ({ onSearchChange, onSortChange }) => {
  return (
    <div className="bg-white p-4 rounded-lg shadow-md mb-6">
      <div className="flex flex-col md:flex-row md:items-center md:space-x-4">
        <div className="mb-4 md:mb-0 flex-grow">
          <label htmlFor="search" className="block text-sm font-medium text-gray-700 mb-1">
            Поиск товаров
          </label>
          <input
            type="text"
            id="search"
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Введите название товара..."
            onChange={(e) => onSearchChange(e.target.value)}
          />
        </div>
        
        <div className="w-full md:w-64">
          <label htmlFor="sort" className="block text-sm font-medium text-gray-700 mb-1">
            Сортировка
          </label>
          <select
            id="sort"
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            onChange={(e) => onSortChange(e.target.value)}
            defaultValue="default"
          >
            <option value="default">По умолчанию</option>
            <option value="price-asc">Цена: по возрастанию</option>
            <option value="price-desc">Цена: по убыванию</option>
            <option value="name-asc">Название: А-Я</option>
            <option value="name-desc">Название: Я-А</option>
          </select>
        </div>
      </div>
    </div>
  );
};

export default ProductFilters; 