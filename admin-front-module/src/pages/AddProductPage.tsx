import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createProduct } from '../api/client';
import { Product } from '../api/dto';

const AddProductPage: React.FC = () => {
  const [newProduct, setNewProduct] = useState<Partial<Product> & { image?: File }>({});
  const [saving, setSaving] = useState(false);
  const [addError, setAddError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleAddChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setNewProduct(prev => ({ ...prev, [name]: name === 'price' || name === 'stockQuantity' ? Number(value) : value }));
  };

  const handleAddFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!e.target.files) return;
    const file = e.target.files[0];
    setNewProduct(prev => ({ ...prev, image: file }));
  };

  const handleAddSave = async () => {
    if (!newProduct.name || !newProduct.price || !newProduct.categoryName) {
      setAddError('Заполните обязательные поля');
      return;
    }
    setSaving(true);
    setAddError(null);
    try {
      const data = {
        name: newProduct.name,
        description: newProduct.description || '',
        price: newProduct.price,
        stockQuantity: newProduct.stockQuantity || 0,
        categoryName: newProduct.categoryName,
        image: newProduct.image,
      };
      await createProduct(data as any);
      navigate('/products');
    } catch {
      setAddError('Ошибка при добавлении');
    } finally {
      setSaving(false);
    }
  };

  const handleCancel = () => {
    navigate('/products');
  };

  return (
    <div className="max-w-md mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-xl font-bold mb-4">Добавить продукт</h2>
      <input
        name="name"
        value={newProduct.name || ''}
        onChange={handleAddChange}
        className="border rounded px-2 py-1 w-full mb-2"
        aria-label="Название товара"
        placeholder="Название товара"
      />
      <input
        name="categoryName"
        value={newProduct.categoryName || ''}
        onChange={handleAddChange}
        className="border rounded px-2 py-1 w-full mb-2"
        aria-label="Категория"
        placeholder="Категория"
      />
      <input
        name="price"
        type="number"
        value={newProduct.price || ''}
        onChange={handleAddChange}
        className="border rounded px-2 py-1 w-full mb-2"
        aria-label="Цена"
        placeholder="Цена"
      />
      <input
        name="stockQuantity"
        type="number"
        value={newProduct.stockQuantity || ''}
        onChange={handleAddChange}
        className="border rounded px-2 py-1 w-full mb-2"
        aria-label="В наличии"
        placeholder="В наличии"
      />
      <textarea
        name="description"
        value={newProduct.description || ''}
        onChange={handleAddChange}
        className="border rounded px-2 py-1 w-full min-h-[60px] mb-2"
        aria-label="Описание"
        placeholder="Описание"
      />
      <input
        type="file"
        accept="image/*"
        onChange={handleAddFileChange}
        className="block mb-2"
        aria-label="Загрузить картинку"
        disabled={saving}
      />
      {addError && <div className="text-red-500 mb-2">{addError}</div>}
      <div className="flex gap-4 mt-2">
        <button
          className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition-colors disabled:opacity-50"
          onClick={handleAddSave}
          disabled={saving}
        >
          {saving ? 'Сохранение...' : 'Сохранить'}
        </button>
        <button
          className="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400 transition-colors"
          onClick={handleCancel}
          disabled={saving}
        >
          Отменить
        </button>
      </div>
    </div>
  );
};

export default AddProductPage; 