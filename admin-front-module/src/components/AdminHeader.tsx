import React, { useState, useRef } from 'react';
import { useAuth } from '../context/AuthContext';
import { Link, useLocation } from 'react-router-dom';

const AdminHeader: React.FC = () => {
  const { user, logout } = useAuth();
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);
  const location = useLocation();

  // Закрытие дропдауна при клике вне
  React.useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setDropdownOpen(false);
      }
    };
    if (dropdownOpen) {
      document.addEventListener('mousedown', handleClickOutside);
    } else {
      document.removeEventListener('mousedown', handleClickOutside);
    }
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [dropdownOpen]);

  const handleLogout = async () => {
    setDropdownOpen(false);
    await logout();
  };

  return (
    <header className="w-full flex items-center justify-between px-8 py-4 bg-gray-100 border-b">
      <nav className="flex items-center gap-6">
        <Link
          to="/products"
          className={`font-semibold text-lg px-2 py-1 rounded transition-colors ${location.pathname === '/products' ? 'bg-blue-100 text-blue-700' : 'hover:bg-gray-200'}`}
        >
          Товары
        </Link>
        <Link
          to="/settings"
          className={`font-semibold text-lg px-2 py-1 rounded transition-colors ${location.pathname === '/settings' ? 'bg-blue-100 text-blue-700' : 'hover:bg-gray-200'}`}
        >
          Настройки
        </Link>
      </nav>
      <div className="relative" ref={dropdownRef}>
        <button
          className="flex items-center gap-2 px-3 py-2 rounded hover:bg-gray-200 focus:outline-none"
          onClick={() => setDropdownOpen((v) => !v)}
          tabIndex={0}
          aria-label="Пользовательское меню"
          onKeyDown={e => { if (e.key === 'Enter' || e.key === ' ') setDropdownOpen(v => !v); }}
        >
          <span className="text-gray-700">{user?.email}</span>
          <svg className={`w-4 h-4 transition-transform ${dropdownOpen ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M19 9l-7 7-7-7" /></svg>
        </button>
        {dropdownOpen && (
          <div className="absolute right-0 mt-2 w-40 bg-white border rounded shadow z-10">
            <button
              className="w-full text-left px-4 py-2 hover:bg-gray-100 text-red-600"
              onClick={handleLogout}
              tabIndex={0}
            >
              Выйти
            </button>
          </div>
        )}
      </div>
    </header>
  );
};

export default AdminHeader; 