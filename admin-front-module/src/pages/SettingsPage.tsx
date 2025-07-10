import React, { useState, useEffect, KeyboardEvent } from 'react';
import { getUsers } from '../api/client';
import { User } from '../api/dto';

const tabs = [
  { key: 'users', label: 'Пользователи' },
  { key: 'system', label: 'Система' },
  { key: 'extra', label: 'Дополнительно' },
];

type TabKey = 'users' | 'system' | 'extra';

const SettingsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<TabKey>('users');
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (activeTab !== 'users') return;
    setLoading(true);
    setError(null);
    getUsers()
      .then(setUsers)
      .catch(() => setError('Ошибка загрузки пользователей'))
      .finally(() => setLoading(false));
  }, [activeTab]);

  const handleTabClick = (key: TabKey) => setActiveTab(key);

  const handleTabKeyDown = (e: KeyboardEvent<HTMLButtonElement>, key: TabKey) => {
    if (e.key === 'Enter' || e.key === ' ') {
      setActiveTab(key);
    }
  };

  const renderTabContent = () => {
    if (activeTab === 'users') {
      if (loading) return <div className="text-gray-500">Загрузка пользователей...</div>;
      if (error) return <div className="text-red-500">{error}</div>;
      return (
        <div>
          <table className="min-w-full border text-sm rounded overflow-hidden">
            <thead>
              <tr className="bg-gray-100">
                <th className="px-4 py-2 text-left font-semibold">ID</th>
                <th className="px-4 py-2 text-left font-semibold">Email</th>
              </tr>
            </thead>
            <tbody>
              {users.map(user => (
                <tr key={user.id} className="border-t hover:bg-blue-50">
                  <td className="px-4 py-2">{user.id}</td>
                  <td className="px-4 py-2">{user.email}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      );
    }
    if (activeTab === 'system') {
      return <div className="text-lg">Системные настройки</div>;
    }
    if (activeTab === 'extra') {
      return <div className="text-lg">Дополнительные параметры</div>;
    }
    return null;
  };

  return (
    <div className="w-full max-w-screen-2xl mx-auto flex bg-white rounded shadow min-h-[500px] px-4">
      <nav className="w-72 border-r flex flex-col py-6" aria-label="Настройки">
        {tabs.map(tab => (
          <button
            key={tab.key}
            className={`text-left px-6 py-3 text-base font-medium focus:outline-none focus:bg-blue-100 transition-colors rounded-none border-l-4 ${
              activeTab === tab.key
                ? 'bg-blue-50 border-blue-500 text-blue-700'
                : 'border-transparent text-gray-700 hover:bg-gray-50'
            }`}
            aria-current={activeTab === tab.key ? 'page' : undefined}
            aria-label={tab.label}
            tabIndex={0}
            onClick={() => handleTabClick(tab.key as TabKey)}
            onKeyDown={e => handleTabKeyDown(e, tab.key as TabKey)}
            type="button"
          >
            {tab.label}
          </button>
        ))}
      </nav>
      <section className="flex-1 p-8">
        <h2 className="text-xl font-bold mb-4">{tabs.find(t => t.key === activeTab)?.label}</h2>
        {renderTabContent()}
      </section>
    </div>
  );
};

export default SettingsPage; 