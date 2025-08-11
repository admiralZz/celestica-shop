import React, { useEffect, useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import { confirmEmail } from '../api/client';
import { useTranslation } from 'react-i18next';

const ActivationPage: React.FC = () => {
  const [status, setStatus] = useState<'pending' | 'success' | 'error'>('pending');
  const [error, setError] = useState<string | null>(null);
  const location = useLocation();
  const { t } = useTranslation();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get('token');
    if (!token) {
      setStatus('error');
      setError(t('activation.invalidLink'));
      return;
    }
    confirmEmail(token)
      .then(() => setStatus('success'))
      .catch((err) => {
        setStatus('error');
        setError(
          err?.response?.data?.message || t('activation.error')
        );
      });
  }, [location.search, t]);

  return (
    <div className="container mx-auto px-4 py-8 text-center">
      {status === 'pending' && <p className="text-lg">{t('activation.confirming')}</p>}
      {status === 'success' && (
        <>
          <p className="text-lg text-green-600 mb-4">{t('activation.emailConfirmed')}</p>
          <Link to="/" className="text-blue-500 hover:underline">{t('activation.toHome')}</Link>
        </>
      )}
      {status === 'error' && (
        <>
          <p className="text-lg text-red-500 mb-4">{error}</p>
          <Link to="/" className="text-blue-500 hover:underline">{t('activation.errorBack')}</Link>
        </>
      )}
    </div>
  );
};

export default ActivationPage; 