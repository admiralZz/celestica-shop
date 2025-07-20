import React, { useEffect, useState } from 'react';
import { getMailSettings, updateMailSettings } from '../../../api/client';

const MailSettingsTab: React.FC = () => {
  const [mailSettings, setMailSettings] = useState<any>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [editMode, setEditMode] = useState(false);
  const [editValues, setEditValues] = useState<any>({});
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState<string | null>(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    getMailSettings(0)
      .then(data => {
        setMailSettings(data);
        setEditValues(data);
      })
      .catch(() => setError('Ошибка загрузки настроек почты'))
      .finally(() => setLoading(false));
  }, []);

  const handleEdit = () => setEditMode(true);
  const handleCancel = () => {
    setEditValues(mailSettings);
    setEditMode(false);
    setSaveError(null);
  };
  const handleChange = (key: string, value: string) => {
    setEditValues((prev: any) => ({ ...prev, [key]: value }));
  };
  const handleSave = async () => {
    setSaving(true);
    setSaveError(null);
    try {
      const updated = await updateMailSettings(editValues);
      setMailSettings(updated);
      setEditValues(updated);
      setEditMode(false);
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
      {Object.entries(editMode ? editValues : mailSettings).map(([key, value]) => (
        <div key={key} className="flex gap-2 items-center">
          <span className="font-semibold min-w-[120px]">{key}:</span>
          {editMode ? (
            <input
              className="border px-2 py-1 rounded w-full max-w-xs"
              value={editValues[key] ?? ''}
              onChange={e => handleChange(key, e.target.value)}
              disabled={saving}
            />
          ) : (
            <span>{String(value)}</span>
          )}
        </div>
      ))}
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