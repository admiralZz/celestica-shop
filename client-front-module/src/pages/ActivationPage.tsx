import React, { useEffect, useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import { confirmEmail } from '../api/client';

const ActivationPage: React.FC = () => {
  const [status, setStatus] = useState<'pending' | 'success' | 'error'>('pending');
  const [error, setError] = useState<string | null>(null);
  const location = useLocation();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get('token');
    if (!token) {
      setStatus('error');
      setError('Некорректная ссылка активации.');
      return;
    }
    confirmEmail(token)
      .then(() => setStatus('success'))
      .catch((err) => {
        setStatus('error');
        setError(
          err?.response?.data?.message || 'Ошибка подтверждения регистрации.'
        );
      });
  }, [location.search]);

  return (
    <div className="container mx-auto px-4 py-8 text-center">
      {status === 'pending' && <p className="text-lg">Подтверждение...</p>}
      {status === 'success' && (
        <>
          <p className="text-lg text-green-600 mb-4">Почта успешно подтверждена! Теперь можно входить в ваш аккаунт</p>
          <Link to="/" className="text-blue-500 hover:underline">Перейти на главную</Link>
        </>
      )}
      {status === 'error' && (
        <>
          <p className="text-lg text-red-500 mb-4">{error}</p>
          <Link to="/" className="text-blue-500 hover:underline">Вернуться на главную</Link>
        </>
      )}
    </div>
  );
};

export default ActivationPage; 