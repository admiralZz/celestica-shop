import React, {useState, useEffect} from 'react';
import ProductCard from './ProductCard';
import ProductFilters from './ProductFilters';
import {getItems} from "../api/client";
import {Product} from "../api/dto";
import { useTranslation } from 'react-i18next';

const ProductCatalog: React.FC = () => {
    const [products, setProducts] = useState<Product[]>([]);
    const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [sortOption, setSortOption] = useState('default');
    const { t } = useTranslation();

    // Загрузка данных с сервера один раз при монтировании
    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const productsList = await getItems();
                setProducts(productsList);
                setFilteredProducts(productsList);
            } catch (error: any) {
                console.error('Ошибка загрузки продуктов:', error.message);
            }
        };

        fetchProducts();
    }, []);

    // Применение фильтрации и сортировки
    useEffect(() => {
        let filteredProducts = [...products];

        // Применяем поиск
        if (searchTerm) {
            filteredProducts = filteredProducts.filter(product =>
                product.name.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }

        // Применяем сортировку
        switch (sortOption) {
            case 'price-asc':
                filteredProducts.sort((a, b) => a.price - b.price);
                break;
            case 'price-desc':
                filteredProducts.sort((a, b) => b.price - a.price);
                break;
            case 'name-asc':
                filteredProducts.sort((a, b) => a.name.localeCompare(b.name));
                break;
            case 'name-desc':
                filteredProducts.sort((a, b) => b.name.localeCompare(a.name));
                break;
            default:
                // По умолчанию сортировка по ID
                filteredProducts.sort((a, b) => a.id - b.id);
        }

        setFilteredProducts(filteredProducts);
    }, [searchTerm, sortOption, products]);

    return (
        <div className="container mx-auto px-4 py-8">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">{t('catalog.title')}</h2>

            <ProductFilters
                onSearchChange={setSearchTerm}
                onSortChange={setSortOption}
            />

            {filteredProducts.length === 0 ? (
                <div className="text-center py-8">
                    <p className="text-gray-500 text-lg">{t('catalog.notFound')}</p>
                </div>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                    {filteredProducts.map(product => (
                        <ProductCard
                            key={product.id}
                            id={product.id}
                            name={product.name}
                            price={product.price}
                            imageUrl={product.imageUrl}
                        />
                    ))}
                </div>
            )}
        </div>
    );
};

export default ProductCatalog; 