import React, { useState, KeyboardEvent } from 'react';
import MailSettingsTab from './tab/MailSettingsTab';
import UsersSettingsTab from './tab/UsersSettingsTab';

const tabs = [
  { key: 'users', label: 'Пользователи' },
  { key: 'system', label: 'Система' },
  { key: 'mail', label: 'Почтовый клиент' },
  { key: 'extra', label: 'Дополнительно' },
];

type TabKey = 'users' | 'system' | 'extra' | 'mail';

const SettingsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<TabKey>('users');

  const handleTabClick = (key: TabKey) => setActiveTab(key);

  const handleTabKeyDown = (e: KeyboardEvent<HTMLButtonElement>, key: TabKey) => {
    if (e.key === 'Enter' || e.key === ' ') {
      setActiveTab(key);
    }
  };

  const renderTabContent = () => {
    if (activeTab === 'users') {
      return <UsersSettingsTab />;
    }
    if (activeTab === 'system') {
      return <div className="text-lg">Системные настройки</div>;
    }
    if (activeTab === 'extra') {
      return <div className="text-lg">Дополнительные параметры</div>;
    }
    if (activeTab === 'mail') {
      return <MailSettingsTab />;
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