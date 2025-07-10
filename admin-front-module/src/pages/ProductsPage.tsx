import React, { useEffect, useState } from 'react';
import { getItems, createProduct } from '../api/client';
import { Product } from '../api/dto';
import { useNavigate } from 'react-router-dom';

const ProductsPage: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const [showAddModal, setShowAddModal] = useState(false);
  const [newProduct, setNewProduct] = useState<Partial<Product> & { image?: File }>({});
  const [saving, setSaving] = useState(false);
  const [addError, setAddError] = useState<string | null>(null);

  const handleRowClick = (id: number) => {
    navigate(`/products/${id}`);
  };

  const handleRowKeyDown = (e: React.KeyboardEvent<HTMLTableRowElement>, id: number) => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      navigate(`/products/${id}`);
    }
  };

  const handleAddClick = () => {
    navigate('/products/add');
  };

  const handleAddCancel = () => {
    setShowAddModal(false);
    setNewProduct({});
    setAddError(null);
  };

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
      setShowAddModal(false);
      setNewProduct({});
      setAddError(null);
      // обновить список
      setLoading(true);
      const items = await getItems();
      setProducts(items);
    } catch {
      setAddError('Ошибка при добавлении');
    } finally {
      setSaving(false);
    }
  };

  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);
      setError(null);
      try {
        const items = await getItems();
        setProducts(items);
      } catch (err: any) {
        setError('Ошибка загрузки товаров');
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
  }, []);

  if (loading) {
    return <div className="flex justify-center items-center h-40">Загрузка товаров...</div>;
  }
  if (error) {
    return <div className="text-red-500 text-center mt-8">{error}</div>;
  }

  return (
    <div className="w-full max-w-screen-2xl mx-auto flex bg-white rounded shadow min-h-[500px] px-4">
      <section className="flex-1 p-8">
        <h2 className="text-xl font-bold mb-4">Товары</h2>
        <button
          className="mb-4 px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition-colors"
          onClick={handleAddClick}
        >
          Добавить продукт
        </button>
        <table className="min-w-full bg-white border rounded shadow">
          <thead>
            <tr>
              <th className="px-4 py-2 border-b">ID</th>
              <th className="px-4 py-2 border-b">Название</th>
              <th className="px-4 py-2 border-b">Категория</th>
              <th className="px-4 py-2 border-b">Цена</th>
              <th className="px-4 py-2 border-b">В наличии</th>
            </tr>
          </thead>
          <tbody>
            {products.map(product => (
              <tr
                key={product.id}
                className="hover:bg-blue-50 cursor-pointer focus:bg-blue-100 outline-none"
                tabIndex={0}
                aria-label={`Открыть товар ${product.name}`}
                onClick={() => handleRowClick(product.id)}
                onKeyDown={e => handleRowKeyDown(e, product.id)}
              >
                <td className="px-4 py-2 border-b">{product.id}</td>
                <td className="px-4 py-2 border-b">{product.name}</td>
                <td className="px-4 py-2 border-b">{product.categoryName}</td>
                <td className="px-4 py-2 border-b">{product.price}</td>
                <td className="px-4 py-2 border-b">{product.stockQuantity}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>
  );
};

export default ProductsPage; 