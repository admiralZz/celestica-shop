import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getItemById, updateProduct, deleteProduct } from '../api/client';
import { Product } from '../api/dto';

type EditableProduct = Omit<Partial<Product>, 'imageUrl'> & { image?: File };

const ProductPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [editMode, setEditMode] = useState(false);
  const [editedProduct, setEditedProduct] = useState<EditableProduct | null>(null);
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState<string | null>(null);
  const [uploadError, setUploadError] = useState<string | null>(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [deleteError, setDeleteError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    const fetchProduct = async () => {
      setLoading(true);
      setError(null);
      try {
        const item = await getItemById(Number(id));
        setProduct(item);
      } catch {
        setError('Товар не найден');
      } finally {
        setLoading(false);
      }
    };
    fetchProduct();
  }, [id]);

  const handleEdit = () => {
    setEditedProduct(product!);
    setEditMode(true);
    setSaveError(null);
  };

  const handleCancel = () => {
    setEditMode(false);
    setEditedProduct(null);
    setSaveError(null);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setEditedProduct(prev => ({ ...prev!, [name]: name === 'price' || name === 'stockQuantity' ? Number(value) : value }));
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!e.target.files) return;
    const file = e.target.files[0];
    setEditedProduct(prev => ({ ...prev!, image: file }));
  };

  const handleSave = async () => {
    if (!editedProduct || !id) return;
    setSaving(true);
    setSaveError(null);
    try {
      const updateData = {
        id: Number(id),
        name: editedProduct.name ?? product!.name,
        description: editedProduct.description ?? product!.description,
        price: editedProduct.price ?? product!.price,
        stockQuantity: editedProduct.stockQuantity ?? product!.stockQuantity,
        categoryName: editedProduct.categoryName ?? product!.categoryName,
        image: editedProduct.image,
      };
      const updated = await updateProduct(Number(id), updateData);
      setProduct(updated);
      setEditMode(false);
      setEditedProduct(null);
    } catch {
      setSaveError('Ошибка при сохранении');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = () => setShowDeleteModal(true);
  const handleDeleteCancel = () => setShowDeleteModal(false);
  const handleDeleteConfirm = async () => {
    if (!id) return;
    setDeleting(true);
    setDeleteError(null);
    try {
      await deleteProduct(Number(id));
      navigate('/products');
    } catch {
      setDeleteError('Ошибка при удалении');
    } finally {
      setDeleting(false);
    }
  };

  if (loading) return <div className="flex justify-center items-center h-40">Загрузка товара...</div>;
  if (error) return <div className="text-red-500 text-center mt-8">{error}</div>;
  if (!product) return null;

  const display = editMode ? editedProduct! : product;

  return (
    <div className="max-w-2xl mx-auto mt-8 p-6 bg-white rounded shadow">
      <h2 className="text-2xl font-bold mb-2">
        {editMode ? (
          <input
            name="name"
            value={display.name}
            onChange={handleChange}
            className="border rounded px-2 py-1 w-full mb-2"
            aria-label="Название товара"
          />
        ) : (
          display.name
        )}
      </h2>
      <img
        src={
          editMode
            ? editedProduct?.image
              ? URL.createObjectURL(editedProduct.image as File)
              : product?.imageUrl || '/static/placeholder.jpg'
            : product?.imageUrl || '/static/placeholder.jpg'
        }
        alt={display.name}
        className="w-full max-h-64 object-contain rounded mb-4"
      />
      {editMode && (
        <div className="mb-2">
          <input
            type="file"
            accept="image/*"
            onChange={handleFileChange}
            className="block"
            aria-label="Загрузить картинку"
            disabled={saving}
          />
          {uploadError && <div className="text-red-500 mt-1">{uploadError}</div>}
        </div>
      )}
      <div className="mb-2 text-gray-600">
        Категория:{' '}
        {editMode ? (
          <input
            name="categoryName"
            value={display.categoryName}
            onChange={handleChange}
            className="border rounded px-2 py-1 w-full"
            aria-label="Категория"
          />
        ) : (
          display.categoryName
        )}
      </div>
      <div className="mb-2">
        Цена:{' '}
        {editMode ? (
          <input
            name="price"
            type="number"
            value={display.price}
            onChange={handleChange}
            className="border rounded px-2 py-1 w-32"
            aria-label="Цена"
          />
        ) : (
          <span className="font-semibold">{display.price}</span>
        )}
      </div>
      <div className="mb-2">
        В наличии:{' '}
        {editMode ? (
          <input
            name="stockQuantity"
            type="number"
            value={display.stockQuantity}
            onChange={handleChange}
            className="border rounded px-2 py-1 w-24"
            aria-label="В наличии"
          />
        ) : (
          display.stockQuantity
        )}
      </div>
      <div className="mb-2 text-gray-700">
        {editMode ? (
          <textarea
            name="description"
            value={display.description}
            onChange={handleChange}
            className="border rounded px-2 py-1 w-full min-h-[80px]"
            aria-label="Описание"
          />
        ) : (
          display.description
        )}
      </div>
      {!editMode && (
        <div className="flex gap-4 mt-4">
          <button
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
            onClick={handleEdit}
          >
            Редактировать
          </button>
          <button
            className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition-colors"
            onClick={handleDelete}
          >
            Удалить
          </button>
        </div>
      )}
      {editMode && (
        <div className="flex gap-4 mt-4">
          <button
            className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition-colors disabled:opacity-50"
            onClick={handleSave}
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
      )}
      {saveError && <div className="text-red-500 mt-2">{saveError}</div>}
      {showDeleteModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-40 z-50">
          <div className="bg-white p-6 rounded shadow max-w-md w-full relative">
            <h3 className="text-lg font-bold mb-4">Удалить продукт?</h3>
            <p className="mb-4">Вы уверены, что хотите удалить этот продукт?</p>
            {deleteError && <div className="text-red-500 mb-2">{deleteError}</div>}
            <div className="flex gap-4 mt-2">
              <button
                className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition-colors disabled:opacity-50"
                onClick={handleDeleteConfirm}
                disabled={deleting}
              >
                {deleting ? 'Удаление...' : 'Удалить'}
              </button>
              <button
                className="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400 transition-colors"
                onClick={handleDeleteCancel}
                disabled={deleting}
              >
                Отменить
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProductPage; 