import React, { useEffect, useState } from 'react';
import { getMailSettings, updateMailSettings } from '../../../api/client';
import type { UpdateMailSettings, MailSettings } from '../../../api/dto';

const MailSettingsTab: React.FC = () => {
  const [mailSettings, setMailSettings] = useState<MailSettings | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [editMode, setEditMode] = useState(false);
  const [editValues, setEditValues] = useState<UpdateMailSettings>({
    host: '',
    port: '',
    username: '',
    password: '',
    protocol: '',
    auth: false,
    sslEnable: false,
  });
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState<string | null>(null);
  const [confirmPassword, setConfirmPassword] = useState('');

  useEffect(() => {
    setLoading(true);
    setError(null);
    getMailSettings(0)
      .then(data => {
        setMailSettings(data);
        setEditValues({
          host: data.host,
          port: data.port,
          username: data.username,
          password: '',
          protocol: data.protocol,
          auth: data.auth,
          sslEnable: data.sslEnable,
        });
      })
      .catch(() => setError('Ошибка загрузки настроек почты'))
      .finally(() => setLoading(false));
  }, []);

  const handleEdit = () => setEditMode(true);
  const handleCancel = () => {
    if (mailSettings) {
      setEditValues({
        host: mailSettings.host,
        port: mailSettings.port,
        username: mailSettings.username,
        password: '',
        protocol: mailSettings.protocol,
        auth: mailSettings.auth,
        sslEnable: mailSettings.sslEnable,
      });
    }
    setEditMode(false);
    setSaveError(null);
    setConfirmPassword('');
  };
  const handleChange = (key: keyof UpdateMailSettings, value: string | boolean) => {
    setEditValues(prev => ({ ...prev, [key]: value }));
  };
  const handleSave = async () => {
    setSaving(true);
    setSaveError(null);
    if (editValues.password && confirmPassword !== editValues.password) {
      setSaveError('Пароли не совпадают');
      setSaving(false);
      return;
    }
    try {
      const updated = await updateMailSettings(editValues);
      setMailSettings(updated);
      setEditValues({
        host: updated.host,
        port: updated.port,
        username: updated.username,
        password: '',
        protocol: updated.protocol,
        auth: updated.auth,
        sslEnable: updated.sslEnable,
      });
      setEditMode(false);
      setConfirmPassword('');
    } catch (e) {
      setSaveError('Ошибка сохранения настроек');
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div className="text-gray-500">Загрузка настроек почты...</div>;
  if (error) return <div className="text-red-500">{error}</div>;
  if (!mailSettings) return <div className="text-gray-400">Нет данных о настройках почты</div>;

  return (
    <div className="space-y-2">
      {Object.entries(editMode ? editValues : mailSettings || {}).map(([key, value]) => {
        if (key === 'password' || key === 'hasPassword') {
          return (
              <div key={key} className="flex gap-2 items-center">
                <span className="font-semibold min-w-[120px]">Пароль:</span>
                {editMode ? (
                    <>
                        <input
                            className="border px-2 py-1 rounded w-full max-w-xs"
                            type="password"
                            value={editValues.password}
                            onChange={e => handleChange('password', e.target.value)}
                            disabled={saving}
                            placeholder="Новый пароль"
                        />
                        <input
                            className="border px-2 py-1 rounded w-full max-w-xs ml-2"
                            type="password"
                            value={confirmPassword}
                            onChange={e => setConfirmPassword(e.target.value)}
                            disabled={saving}
                            placeholder="Подтвердите пароль"
                            aria-label="Подтвердите пароль"
                        />
                    </>
                ) : (
                    <span>
            {mailSettings && mailSettings.hasPassword ? '••••••••' : '(не задан)'}
          </span>
                )}
              </div>
          );
        }

        return (
            <div key={key} className="flex gap-2 items-center">
              <span className="font-semibold min-w-[120px]">{key}:</span>
              {editMode ? (
                  <input
                      className="border px-2 py-1 rounded w-full max-w-xs"
                      value={String(value)}
                      onChange={e => handleChange(key as keyof UpdateMailSettings, e.target.value)}
                      disabled={saving}
                  />
              ) : (
                  <span>{String(value)}</span>
              )}
            </div>
        );
      })}
      <div className="mb-2">
        {editMode ? (
          <>
            <button className="px-3 py-1 bg-blue-600 text-white rounded mr-2 disabled:opacity-60" onClick={handleSave} disabled={saving}>Сохранить</button>
            <button className="px-3 py-1 bg-gray-300 text-gray-800 rounded" onClick={handleCancel} disabled={saving}>Отмена</button>
            {saveError && <span className="ml-4 text-red-500">{saveError}</span>}
          </>
        ) : (
          <button className="px-3 py-1 bg-blue-600 text-white rounded" onClick={handleEdit}>Редактировать</button>
        )}
      </div>
    </div>
  );
};

export default MailSettingsTab; 