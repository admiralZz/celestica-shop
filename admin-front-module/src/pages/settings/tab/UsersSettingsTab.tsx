import React, { useEffect, useState } from 'react';
import { getUsers } from '../../../api/client';
import { User } from '../../../api/dto';

const UsersSettingsTab: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    getUsers()
      .then(setUsers)
      .catch(() => setError('Ошибка загрузки пользователей'))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="text-gray-500">Загрузка пользователей...</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div>
      <table className="min-w-full border text-sm rounded overflow-hidden">
        <thead>
          <tr className="bg-gray-100">
            <th className="px-4 py-2 text-left font-semibold">ID</th>
            <th className="px-4 py-2 text-left font-semibold">Email</th>
            <th className="px-4 py-2 text-left font-semibold">Дата создания</th>
          </tr>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.id} className="border-t hover:bg-blue-50">
              <td className="px-4 py-2">{user.id}</td>
              <td className="px-4 py-2">{user.email}</td>
              <td className="px-4 py-2">{user.createdAt?.toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default UsersSettingsTab; 