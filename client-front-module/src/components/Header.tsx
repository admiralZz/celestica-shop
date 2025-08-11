import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Logo from './Logo';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';

type HeaderProps = {
  onAuthClick?: () => void;
};

const Header: React.FC<HeaderProps> = ({ onAuthClick }) => {
  const { getTotalItems } = useCart();
  const itemCount = getTotalItems();
  const { user, isAuthenticated, logout } = useAuth();
  const [dropdownOpen, setDropdownOpen] = React.useState(false);
  const dropdownRef = React.useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = React.useState(false);
  const { t, i18n } = useTranslation();

  // language dropdown (desktop)
  const [langDropdownOpen, setLangDropdownOpen] = React.useState(false);
  const langDropdownRef = React.useRef<HTMLDivElement>(null);

  // language dropdown (mobile)
  const [langDropdownOpenMobile, setLangDropdownOpenMobile] = React.useState(false);
  const langDropdownMobileRef = React.useRef<HTMLDivElement>(null);

  const changeLang = (lng: 'en' | 'ru' | '中文') => {
    i18n.changeLanguage(lng);
    localStorage.setItem('lang', lng);
    setLangDropdownOpen(false);
    setLangDropdownOpenMobile(false);
  };

  React.useEffect(() => {
    if (!dropdownOpen) return;
    const handleClick = (e: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target as Node)) {
        setDropdownOpen(false);
      }
    };
    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === 'Escape') setDropdownOpen(false);
    };
    document.addEventListener('mousedown', handleClick);
    document.addEventListener('keydown', handleEsc);
    return () => {
      document.removeEventListener('mousedown', handleClick);
      document.removeEventListener('keydown', handleEsc);
    };
  }, [dropdownOpen]);

  React.useEffect(() => {
    if (!langDropdownOpen) return;
    const handleClick = (e: MouseEvent) => {
      if (langDropdownRef.current && !langDropdownRef.current.contains(e.target as Node)) {
        setLangDropdownOpen(false);
      }
    };
    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === 'Escape') setLangDropdownOpen(false);
    };
    document.addEventListener('mousedown', handleClick);
    document.addEventListener('keydown', handleEsc);
    return () => {
      document.removeEventListener('mousedown', handleClick);
      document.removeEventListener('keydown', handleEsc);
    };
  }, [langDropdownOpen]);

  React.useEffect(() => {
    if (!langDropdownOpenMobile) return;
    const handleClick = (e: MouseEvent) => {
      if (langDropdownMobileRef.current && !langDropdownMobileRef.current.contains(e.target as Node)) {
        setLangDropdownOpenMobile(false);
      }
    };
    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === 'Escape') setLangDropdownOpenMobile(false);
    };
    document.addEventListener('mousedown', handleClick);
    document.addEventListener('keydown', handleEsc);
    return () => {
      document.removeEventListener('mousedown', handleClick);
      document.removeEventListener('keydown', handleEsc);
    };
  }, [langDropdownOpenMobile]);
  
  const handleProfile = () => {
    setDropdownOpen(false);
    navigate('/profile');
  };
  const handleLogout = async () => {
    setDropdownOpen(false);
    await logout();
  };
  
  return (
    <header 
      className="bg-gray-800 fixed top-0 left-0 right-0 z-50 shadow-md py-4"
    >
      <div className="container mx-auto px-4 flex items-center">
        <Link to="/">
          <Logo className="text-white" size="normal" />
        </Link>
        {/* Desktop nav */}
        <nav className="ml-auto items-center hidden sm:flex">
          <ul className="flex flex-nowrap space-x-6 text-white">
            {isAuthenticated ? (
              <li className="relative">
                <div ref={dropdownRef}>
                  <button
                    className="flex items-center space-x-2 hover:text-blue-300 transition-colors focus:outline-none"
                    onClick={() => setDropdownOpen(v => !v)}
                    tabIndex={0}
                    aria-label={t('header.userMenu')}
                    aria-haspopup="true"
                    aria-expanded={dropdownOpen}
                    onKeyDown={e => (e.key === 'Enter' || e.key === ' ') && setDropdownOpen(v => !v)}
                  >
                    <span className="text-blue-300">{user?.email}</span>
                    <svg className={`w-4 h-4 ml-1 transition-transform ${dropdownOpen ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </button>
                  {dropdownOpen && (
                    <div className="absolute right-0 mt-2 w-40 bg-white rounded shadow-lg py-2 z-50 text-gray-800 animate-fade-in" role="menu">
                      <button
                        className="w-full text-left px-4 py-2 hover:bg-gray-100 focus:outline-none"
                        onClick={handleProfile}
                        tabIndex={0}
                        role="menuitem"
                      >
                        {t('header.profile')}
                      </button>
                      <button
                        className="w-full text-left px-4 py-2 hover:bg-gray-100 focus:outline-none"
                        onClick={handleLogout}
                        tabIndex={0}
                        role="menuitem"
                      >
                        {t('header.logout')}
                      </button>
                    </div>
                  )}
                </div>
              </li>
            ) : (
              <li>
                <button
                  className="hover:text-blue-300 transition-colors focus:outline-none flex items-center"
                  onClick={onAuthClick}
                  tabIndex={0}
                  aria-label={t('header.auth')}
                  onKeyDown={e => (e.key === 'Enter' || e.key === ' ') && onAuthClick && onAuthClick()}
                >
                  <svg className="h-5 w-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <rect x="5" y="11" width="14" height="8" rx="2" strokeWidth="2" />
                    <path d="M7 11V7a5 5 0 0110 0v4" strokeWidth="2" />
                  </svg>
                  {t('header.auth')}
                </button>
              </li>
            )}
          </ul>
          <Link 
            to="/cart" 
            className="ml-6 flex items-center text-white hover:text-blue-300 transition-colors relative"
            aria-label={t('header.cart')}
          >
            <svg 
              xmlns="http://www.w3.org/2000/svg" 
              className="h-6 w-6" 
              fill="none" 
              viewBox="0 0 24 24" 
              stroke="currentColor"
            >
              <path 
                strokeLinecap="round" 
                strokeLinejoin="round" 
                strokeWidth={2} 
                d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" 
              />
            </svg>
            <span className="ml-2 hidden sm:inline">{t('header.cart')}</span>
            {itemCount > 0 && (
              <span className="absolute -top-2 -right-2 bg-blue-500 text-white text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center">
                {itemCount}
              </span>
            )}
          </Link>
          {/* Lang switcher dropdown (desktop) */}
          <div className="ml-4 relative" ref={langDropdownRef}>
            <button
              className="flex items-center gap-1 bg-gray-700 hover:bg-gray-600 text-white px-3 py-1.5 rounded"
              onClick={() => setLangDropdownOpen(v => !v)}
              aria-haspopup="true"
              aria-expanded={langDropdownOpen}
              aria-label="Language selector"
            >
              <span className="font-semibold">{(i18n.language || 'en').toUpperCase()}</span>
              <svg className={`w-4 h-4 transition-transform ${langDropdownOpen ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
              </svg>
            </button>
            {langDropdownOpen && (
              <div className="absolute right-0 mt-2 w-28 bg-white rounded shadow-lg py-1 z-50 text-gray-800">
                <button
                  className={`w-full text-left px-3 py-2 hover:bg-gray-100 ${i18n.language === 'ru' ? 'font-semibold text-blue-600' : ''}`}
                  onClick={() => changeLang('ru')}
                >RU</button>
                <button
                  className={`w-full text-left px-3 py-2 hover:bg-gray-100 ${i18n.language === 'en' ? 'font-semibold text-blue-600' : ''}`}
                  onClick={() => changeLang('en')}
                >EN</button>
                <button
                  className={`w-full text-left px-3 py-2 hover:bg-gray-100 ${i18n.language === '中文' ? 'font-semibold text-blue-600' : ''}`}
                  onClick={() => changeLang('中文')}
                >中文</button>
              </div>
            )}
          </div>
        </nav>
        {/* Mobile burger */}
        <button
          className="ml-auto flex sm:hidden items-center text-white focus:outline-none z-50"
          onClick={() => setMobileMenuOpen(v => !v)}
          aria-label="Открыть меню"
        >
          <svg className="h-7 w-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
        {/* Mobile menu overlay */}
        {mobileMenuOpen && (
          <div className="fixed inset-0 bg-black bg-opacity-60 z-40 flex sm:hidden" onClick={() => setMobileMenuOpen(false)}>
            <div className="bg-gray-800 w-3/4 max-w-xs h-full p-6 flex flex-col space-y-6" onClick={e => e.stopPropagation()}>
              <ul className="flex flex-col space-y-4 text-white">
                {isAuthenticated ? (
                  <li>
                    <button
                      className="w-full text-left hover:text-blue-300 transition-colors focus:outline-none"
                      onClick={() => { setMobileMenuOpen(false); handleProfile(); }}
                    >
                      {t('header.profile')}
                    </button>
                    <button
                      className="w-full text-left hover:text-blue-300 transition-colors focus:outline-none mt-2"
                      onClick={() => { setMobileMenuOpen(false); handleLogout(); }}
                    >
                      {t('header.logout')}
                    </button>
                  </li>
                ) : (
                  <li>
                    <button
                      className="hover:text-blue-300 transition-colors focus:outline-none flex items-center"
                      onClick={() => { setMobileMenuOpen(false); onAuthClick && onAuthClick(); }}
                    >
                      <svg className="h-5 w-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <rect x="5" y="11" width="14" height="8" rx="2" strokeWidth="2" />
                        <path d="M7 11V7a5 5 0 0110 0v4" strokeWidth="2" />
                      </svg>
                      {t('header.auth')}
                    </button>
                  </li>
                )}
              </ul>
              <Link 
                to="/cart" 
                className="flex items-center text-white hover:text-blue-300 transition-colors relative mt-auto"
                aria-label={t('header.cart')}
                onClick={() => setMobileMenuOpen(false)}
              >
                <svg 
                  xmlns="http://www.w3.org/2000/svg" 
                  className="h-6 w-6" 
                  fill="none" 
                  viewBox="0 0 24 24" 
                  stroke="currentColor"
                >
                  <path 
                    strokeLinecap="round" 
                    strokeLinejoin="round" 
                    strokeWidth={2} 
                    d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 1.5 1.5 0 013 0z" 
                  />
                </svg>
                <span className="ml-2">{t('header.cart')}</span>
                {itemCount > 0 && (
                  <span className="absolute -top-2 -right-2 bg-blue-500 text-white text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center">
                    {itemCount}
                  </span>
                )}
              </Link>
              {/* Lang switcher dropdown (mobile) */}
              <div className="relative" ref={langDropdownMobileRef}>
                <button
                  className="flex items-center gap-1 bg-gray-700 hover:bg-gray-600 text-white px-3 py-2 rounded w-full justify-between"
                  onClick={() => setLangDropdownOpenMobile(v => !v)}
                  aria-haspopup="true"
                  aria-expanded={langDropdownOpenMobile}
                  aria-label="Language selector"
                >
                  <span className="font-semibold">{(i18n.language || 'en').toUpperCase()}</span>
                  <svg className={`w-4 h-4 transition-transform ${langDropdownOpenMobile ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                  </svg>
                </button>
                {langDropdownOpenMobile && (
                  <div className="absolute left-0 right-0 mt-2 bg-white rounded shadow-lg py-1 z-50 text-gray-800">
                    <button
                      className={`w-full text-left px-3 py-2 hover:bg-gray-100 ${i18n.language === 'ru' ? 'font-semibold text-blue-600' : ''}`}
                      onClick={() => changeLang('ru')}
                    >RU</button>
                    <button
                      className={`w-full text-left px-3 py-2 hover:bg-gray-100 ${i18n.language === 'en' ? 'font-semibold text-blue-600' : ''}`}
                      onClick={() => changeLang('en')}
                    >EN</button>
                    <button
                      className={`w-full text-left px-3 py-2 hover:bg-gray-100 ${i18n.language === '中文' ? 'font-semibold text-blue-600' : ''}`}
                      onClick={() => changeLang('中文')}
                    >中文</button>
                  </div>
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header; 