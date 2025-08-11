import React from 'react';
import { useTranslation } from 'react-i18next';

interface ProductFiltersProps {
  onSearchChange: (value: string) => void;
  onSortChange: (value: string) => void;
}

const ProductFilters: React.FC<ProductFiltersProps> = ({ onSearchChange, onSortChange }) => {
  const { t } = useTranslation();
  return (
    <div className="bg-white p-4 rounded-lg shadow-md mb-6">
      <div className="flex flex-col md:flex-row md:items-center md:space-x-4">
        <div className="mb-4 md:mb-0 flex-grow">
          <label htmlFor="search" className="block text-sm font-medium text-gray-700 mb-1">
            {t('filters.searchLabel')}
          </label>
          <input
            type="text"
            id="search"
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder={t('filters.searchPlaceholder')}
            onChange={(e) => onSearchChange(e.target.value)}
          />
        </div>
        
        <div className="w-full md:w-64">
          <label htmlFor="sort" className="block text-sm font-medium text-gray-700 mb-1">
            {t('filters.sortLabel')}
          </label>
          <select
            id="sort"
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            onChange={(e) => onSortChange(e.target.value)}
            defaultValue="default"
          >
            <option value="default">{t('filters.default')}</option>
            <option value="price-asc">{t('filters.priceAsc')}</option>
            <option value="price-desc">{t('filters.priceDesc')}</option>
            <option value="name-asc">{t('filters.nameAsc')}</option>
            <option value="name-desc">{t('filters.nameDesc')}</option>
          </select>
        </div>
      </div>
    </div>
  );
};

export default ProductFilters; 