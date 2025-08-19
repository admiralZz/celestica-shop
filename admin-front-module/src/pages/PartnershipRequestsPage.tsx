import React from 'react';
import { getPartnerRequests } from '../api/client';
import { PartnershipRequest } from '../api/dto';

const columns = [
  { key: 'firstName', label: 'Имя' },
  { key: 'lastName', label: 'Фамилия' },
  { key: 'company', label: 'Компания' },
  { key: 'email', label: 'Email' },
  { key: 'phone', label: 'Телефон' },
  { key: 'location', label: 'Локация' },
  { key: 'cooperationType', label: 'Тип сотрудничества' },
  { key: 'productCategory', label: 'Категория продукта' },
];

type ColKey = keyof PartnershipRequest | 'cooperationType' | 'productCategory';

const DEFAULT_WIDTH = 160;

const PartnershipRequestsPage: React.FC = () => {
  const [requests, setRequests] = React.useState<PartnershipRequest[]>([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState<string | null>(null);
  const [colWidths, setColWidths] = React.useState<Record<ColKey, number>>(() => {
    const obj: Record<ColKey, number> = {} as any;
    columns.forEach(c => { obj[c.key as ColKey] = DEFAULT_WIDTH; });
    return obj;
  });
  const resizing = React.useRef<{key: ColKey, startX: number, startWidth: number} | null>(null);
  const [openedIndex, setOpenedIndex] = React.useState<number | null>(null);

  React.useEffect(() => {
    getPartnerRequests()
      .then(setRequests)
      .catch(() => setError('Ошибка загрузки заявок'))
      .finally(() => setLoading(false));
  }, []);

  // Resize
  const handleMouseDown = (key: ColKey) => (e: React.MouseEvent) => {
    resizing.current = { key, startX: e.clientX, startWidth: colWidths[key] };
    document.addEventListener('mousemove', handleMouseMove);
    document.addEventListener('mouseup', handleMouseUp);
  };
  const handleMouseMove = (e: MouseEvent) => {
    if (!resizing.current) return;
    const { key, startX, startWidth } = resizing.current;
    const delta = e.clientX - startX;
    setColWidths(w => ({ ...w, [key]: Math.max(60, startWidth + delta) }));
  };
  const handleMouseUp = () => {
    resizing.current = null;
    document.removeEventListener('mousemove', handleMouseMove);
    document.removeEventListener('mouseup', handleMouseUp);
  };

  if (loading) return <div className="p-8">Загрузка...</div>;
  if (error) return <div className="p-8 text-red-600">{error}</div>;
  if (!requests.length) return <div className="p-8">Нет заявок на сотрудничество.</div>;

  return (
    <div className="w-full max-w-screen-2xl mx-auto flex bg-white rounded shadow min-h-[500px] px-4">
      <section className="flex-1 p-8">
        <h2 className="text-xl font-bold mb-4">Заявки на сотрудничество</h2>
        <div className="overflow-x-auto">
          <table className="min-w-full border text-sm select-none rounded overflow-hidden">
            <thead className="bg-gray-100">
              <tr>
                {columns.map(col => (
                  <th
                    key={col.key}
                    className="border px-3 py-2 relative group"
                    style={{ width: colWidths[col.key as ColKey], minWidth: 60, maxWidth: 600 }}
                    tabIndex={0}
                    aria-label={`Изменить ширину столбца ${col.label}`}
                  >
                    <div className="flex items-center justify-between">
                      <span>{col.label}</span>
                      <span
                        className="absolute right-0 top-0 h-full w-2 cursor-col-resize z-10 group-hover:bg-blue-200"
                        onMouseDown={handleMouseDown(col.key as ColKey)}
                        tabIndex={-1}
                        aria-label={`Изменить ширину столбца ${col.label}`}
                      />
                    </div>
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {requests.map((r, i) => (
                <React.Fragment key={i}>
                  <tr
                    className={`even:bg-gray-50 cursor-pointer transition hover:bg-blue-50 ${openedIndex === i ? 'bg-blue-50' : ''}`}
                    onClick={() => setOpenedIndex(openedIndex === i ? null : i)}
                    tabIndex={0}
                    aria-label="Показать детали заявки"
                  >
                    {columns.map(col => (
                      <td key={col.key} className="border px-3 py-2" style={{ width: colWidths[col.key as ColKey], minWidth: 60, maxWidth: 600 }}>
                        {col.key === 'firstName' && r.firstName}
                        {col.key === 'lastName' && r.lastName}
                        {col.key === 'company' && r.company}
                        {col.key === 'email' && r.email}
                        {col.key === 'phone' && (r.phone || '-')}
                        {col.key === 'location' && (r.location || '-')}
                        {col.key === 'cooperationType' && r.cooperationType}
                        {col.key === 'productCategory' && (r.productCategory || '-')}
                      </td>
                    ))}
                  </tr>
                  {openedIndex === i && (
                    <tr>
                      <td colSpan={columns.length} className="p-0 bg-blue-50">
                        <div className="flex flex-col md:flex-row gap-4 p-6 border-t border-blue-200">
                          <div className="flex-1 grid grid-cols-1 gap-2 max-w-md">
                            <div><span className="font-semibold">Имя:</span> {r.firstName}</div>
                            <div><span className="font-semibold">Фамилия:</span> {r.lastName}</div>
                            <div><span className="font-semibold">Компания:</span> {r.company}</div>
                            <div><span className="font-semibold">Email:</span> {r.email}</div>
                            <div><span className="font-semibold">Телефон:</span> {r.phone || '-'}</div>
                            <div><span className="font-semibold">Локация:</span> {r.location || '-'}</div>
                            <div><span className="font-semibold">Тип сотрудничества:</span> {r.cooperationType}</div>
                            <div><span className="font-semibold">Категория продукта:</span> {r.productCategory || '-'}</div>
                          </div>
                          <div className="flex-1 md:max-w-xl flex flex-col">
                            <div className="font-semibold mb-1">Сообщение:</div>
                            <div
                              className="bg-white rounded border p-4 whitespace-pre-line break-words shadow-inner min-h-[180px] max-h-[400px] resize-y overflow-auto text-gray-800"
                              style={{ fontFamily: 'inherit' }}
                              tabIndex={0}
                              aria-label="Текст сообщения"
                              contentEditable={false}
                            >
                              {r.message || <span className="text-gray-400">Нет сообщения</span>}
                            </div>
                          </div>
                        </div>
                      </td>
                    </tr>
                  )}
                </React.Fragment>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
};

export default PartnershipRequestsPage; 