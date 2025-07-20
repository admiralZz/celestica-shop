import React from 'react';
import { useAuth } from './context/AuthContext';
import LoginForm from './components/LoginForm';
import AdminHeader from './components/AdminHeader';
import ProductsPage from './pages/ProductsPage';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import ProductPage from './pages/ProductPage';
import AddProductPage from './pages/AddProductPage';
import SettingsPage from './pages/settings/SettingsPage';
import OrdersPage from './pages/OrdersPage';

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
          <Route path="/orders" element={<OrdersPage />} />
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