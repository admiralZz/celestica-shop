import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';

const LoginForm: React.FC = () => {
  const { login, error, loading } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await login({ email, password });
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-4 max-w-xs mx-auto mt-32 p-6 bg-white rounded shadow">
      <input
        className="border p-2 rounded"
        type="email"
        placeholder="Email"
        value={email}
        onChange={e => setEmail(e.target.value)}
        required
        autoFocus
      />
      <input
        className="border p-2 rounded"
        type="password"
        placeholder="Пароль"
        value={password}
        onChange={e => setPassword(e.target.value)}
        required
      />
      <button
        type="submit"
        className="bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
        disabled={loading}
      >
        {loading ? 'Вход...' : 'Войти'}
      </button>
      {error && <div className="text-red-500 text-sm">{error}</div>}
    </form>
  );
};

export default LoginForm; 