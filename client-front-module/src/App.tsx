import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import ProductCatalog from './components/ProductCatalog';
import ProductPage from './pages/ProductPage';
import CartPage from './pages/CartPage';
import CheckoutPage from './pages/CheckoutPage';
import { CartProvider } from './context/CartContext';
import AuthModal from './components/AuthModal';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import ProfilePage from './pages/ProfilePage';

const App: React.FC = () => {
  const [isAuthOpen, setIsAuthOpen] = React.useState(false);
  return (
    <CartProvider>
      <AuthProvider>
        <Router>
          <div className="min-h-screen flex flex-col bg-gray-100">
            <Header onAuthClick={() => setIsAuthOpen(true)} />
            <main className="flex-grow pt-20">
              <Routes>
                <Route path="/" element={<ProductCatalog />} />
                <Route path="/product/:id" element={<ProductPage />} />
                <Route path="/cart" element={<CartPage />} />
                <Route path="/checkout" element={<CheckoutPage />} />
                <Route path="/profile" element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
              </Routes>
            </main>
            <Footer />
            <AuthModal isOpen={isAuthOpen} onClose={() => setIsAuthOpen(false)} />
          </div>
        </Router>
      </AuthProvider>
    </CartProvider>
  );
};

export default App; 