import React from 'react';
import { useAuth } from './context/AuthContext';
import LoginForm from './components/LoginForm';
import AdminHeader from './components/AdminHeader';
import ProductsPage from './pages/ProductsPage';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import ProductPage from './pages/ProductPage';
import AddProductPage from './pages/AddProductPage';

const SettingsPage: React.FC = () => (
  <div className="max-w-4xl mx-auto mt-8">
    <h2 className="text-xl font-bold mb-4">Настройки</h2>
    <div className="text-gray-500">Здесь будут настройки...</div>
  </div>
);

const App: React.FC = () => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Загрузка...</div>;
  }

  if (!isAuthenticated) {
    return <LoginForm />;
  }

  return (
    <Router>
      <AdminHeader />
      <main className="flex-grow pt-20">
        <Routes>
          <Route path="/products" element={<ProductsPage />} />
          <Route path="/products/add" element={<AddProductPage />} />
          <Route path="/products/:id" element={<ProductPage />} />
          <Route path="/settings" element={<SettingsPage />} />
          <Route path="*" element={<Navigate to="/products" replace />} />
        </Routes>
      </main>
    </Router>
  );
};

export default App; 