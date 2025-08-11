import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';

const TABS = [
  { key: 'login', label: 'Вход' },
  { key: 'register', label: 'Регистрация' },
];

type AuthModalProps = {
  isOpen: boolean;
  onClose: () => void;
};

const AuthModal: React.FC<AuthModalProps> = ({ isOpen, onClose }) => {
  const [activeTab, setActiveTab] = useState<'login' | 'register'>('login');
  const [registerPassword, setRegisterPassword] = useState('');
  const [registerConfirm, setRegisterConfirm] = useState('');
  const [registerName, setRegisterName] = useState('');
  const [registerEmail, setRegisterEmail] = useState('');
  const [loginEmail, setLoginEmail] = useState('');
  const [loginPassword, setLoginPassword] = useState('');
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const passwordsMatch = registerPassword === registerConfirm;
  const { login, register, loading, error, isAuthenticated } = useAuth();
  const { t } = useTranslation();

  React.useEffect(() => {
    if (isAuthenticated && isOpen) onClose();
    // eslint-disable-next-line
  }, [isAuthenticated]);

  if (!isOpen) return null;

  const handleOverlayClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) onClose();
  };

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    await login({ email: loginEmail, password: loginPassword });
  };

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!passwordsMatch) return;
    await register({ email: registerEmail, password: registerPassword });
    setSuccessMessage(t('auth.success'));
    setActiveTab('login');
  };

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
      onClick={handleOverlayClick}
      aria-modal="true"
      role="dialog"
      tabIndex={0}
      aria-label="Auth modal"
      onKeyDown={e => e.key === 'Escape' && onClose()}
    >
      <div className="bg-white rounded-lg shadow-lg w-full max-w-md p-6 relative">
        <button
          className="absolute top-2 right-2 text-gray-400 hover:text-gray-700 focus:outline-none"
          onClick={onClose}
          aria-label="Close"
        >
          <svg className="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
        <div className="flex border-b mb-6">
          {TABS.map(tab => (
            <button
              key={tab.key}
              className={`flex-1 py-2 text-center font-semibold focus:outline-none transition-colors ${activeTab === tab.key ? 'border-b-2 border-blue-500 text-blue-600' : 'text-gray-500'}`}
              onClick={() => setActiveTab(tab.key as 'login' | 'register')}
              aria-selected={activeTab === tab.key}
              role="tab"
              tabIndex={0}
            >
              {tab.key === 'login' ? t('auth.login') : t('auth.register')}
            </button>
          ))}
        </div>
        {activeTab === 'login' ? (
          <>
            {successMessage && (
              <div className="text-green-600 text-center mb-2 text-sm">{successMessage}</div>
            )}
            <form className="flex flex-col gap-4" aria-label="Login form" onSubmit={handleLogin}>
              <input
                type="email"
                className="border rounded px-3 py-2"
                placeholder={t('auth.email')}
                required
                autoFocus
                value={loginEmail}
                onChange={e => setLoginEmail(e.target.value)}
              />
              <input
                type="password"
                className="border rounded px-3 py-2"
                placeholder={t('auth.password')}
                required
                value={loginPassword}
                onChange={e => setLoginPassword(e.target.value)}
              />
              {error && <span className="text-red-500 text-sm">{error}</span>}
              <button
                type="submit"
                className="bg-blue-600 text-white rounded py-2 font-semibold hover:bg-blue-700 transition-colors disabled:opacity-50"
                disabled={loading}
              >
                {loading ? t('auth.signingIn') : t('auth.signIn')}
              </button>
            </form>
          </>
        ) : (
          <form className="flex flex-col gap-4" aria-label="Register form" onSubmit={handleRegister}>
            <input
              type="text"
              className="border rounded px-3 py-2"
              placeholder={t('auth.name')}
              required
              autoFocus
              value={registerName}
              onChange={e => setRegisterName(e.target.value)}
            />
            <input
              type="email"
              className="border rounded px-3 py-2"
              placeholder={t('auth.email')}
              required
              value={registerEmail}
              onChange={e => setRegisterEmail(e.target.value)}
            />
            <input
              type="password"
              className="border rounded px-3 py-2"
              placeholder={t('auth.password')}
              required
              value={registerPassword}
              onChange={e => setRegisterPassword(e.target.value)}
            />
            <input
              type="password"
              className={`border rounded px-3 py-2 ${registerConfirm && !passwordsMatch ? 'border-red-500' : ''}`}
              placeholder={t('auth.confirmPassword')}
              required
              value={registerConfirm}
              onChange={e => setRegisterConfirm(e.target.value)}
            />
            {!passwordsMatch && registerConfirm && (
              <span className="text-red-500 text-sm">{t('auth.passwordsMismatch')}</span>
            )}
            {error && <span className="text-red-500 text-sm">{error}</span>}
            <button
              type="submit"
              className="bg-blue-600 text-white rounded py-2 font-semibold hover:bg-blue-700 transition-colors disabled:opacity-50"
              disabled={!passwordsMatch || loading}
            >
              {loading ? t('auth.registering') : t('auth.signUp')}
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

export default AuthModal; 