import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { createPartnershipRequest } from '../api/client';

const PartnerPage: React.FC = () => {
  const { t } = useTranslation();
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    company: '',
    email: '',
    phone: '',
    location: '',
    cooperationType: '',
    productCategory: '',
    message: '',
  });
  const [submitted, setSubmitted] = useState(false);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
  const [loading, setLoading] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  const cooperationOptions = [
    { value: 'supplier', label: t('partnerForm.cooperationOptions.supplier') },
    { value: 'distributor', label: t('partnerForm.cooperationOptions.distributor') },
    { value: 'manufacturer', label: t('partnerForm.cooperationOptions.manufacturer') },
    { value: 'wholesaler', label: t('partnerForm.cooperationOptions.wholesaler') },
    { value: 'other', label: t('partnerForm.cooperationOptions.other') },
  ];

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
    if (errors[name]) {
      const newErrors = { ...errors };
      delete newErrors[name];
      setErrors(newErrors);
    }
  };

  const validate = () => {
    const newErrors: { [key: string]: string } = {};
    if (!form.firstName || form.firstName.length < 2) newErrors.firstName = t('partnerForm.errors.firstName');
    if (!form.lastName || form.lastName.length < 2) newErrors.lastName = t('partnerForm.errors.lastName');
    if (!form.company) newErrors.company = t('partnerForm.errors.company');
    if (!form.cooperationType) newErrors.cooperationType = t('partnerForm.errors.cooperationType');
    if (!form.email) newErrors.email = t('partnerForm.errors.emailRequired');
    else if (!/^\S+@\S+\.\S+$/.test(form.email)) newErrors.email = t('partnerForm.errors.emailFormat');
    return newErrors;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const errs = validate();
    setErrors(errs);
    if (Object.keys(errs).length === 0) {
      setLoading(true);
      setSubmitError(null);
      try {
        await createPartnershipRequest(form);
        setSubmitted(true);
      } catch (err) {
        setSubmitError('Ошибка отправки запроса');
      } finally {
        setLoading(false);
      }
    }
  };

  if (submitted) {
    return (
      <div className="container mx-auto px-4 py-12 text-center">
        <h1 className="text-2xl font-bold mb-6 text-gray-800">{t('partnerForm.title')}</h1>
        <div className="bg-green-100 text-green-800 rounded-lg p-6 inline-block">
          {t('partnerForm.success')}
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-12 max-w-2xl">
      <h1 className="text-2xl font-bold mb-6 text-gray-800">{t('partnerForm.title')}</h1>
      <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow-lg p-8 space-y-6">
        <div>
          <label className="block font-semibold mb-1">{t('partnerForm.firstName')} *</label>
          <input name="firstName" value={form.firstName} onChange={handleChange} className={`w-full border rounded px-3 py-2${errors.firstName ? ' border-red-500' : ''}`} placeholder={t('partnerForm.firstNamePlaceholder')} />
          {errors.firstName && <div className="text-red-500 text-sm mt-1 flex items-center"><svg className="w-5 h-5 mr-1 text-red-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" strokeWidth="2"/><line x1="12" y1="8" x2="12" y2="12" strokeWidth="2"/><circle cx="12" cy="16" r="1" strokeWidth="2"/></svg>{errors.firstName}</div>}
        </div>
        <div>
          <label className="block font-semibold mb-1">{t('partnerForm.lastName')} *</label>
          <input name="lastName" value={form.lastName} onChange={handleChange} className={`w-full border rounded px-3 py-2${errors.lastName ? ' border-red-500' : ''}`} placeholder={t('partnerForm.lastNamePlaceholder')} />
          {errors.lastName && <div className="text-red-500 text-sm mt-1 flex items-center"><svg className="w-5 h-5 mr-1 text-red-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" strokeWidth="2"/><line x1="12" y1="8" x2="12" y2="12" strokeWidth="2"/><circle cx="12" cy="16" r="1" strokeWidth="2"/></svg>{errors.lastName}</div>}
        </div>
        <div>
          <label className="block font-semibold mb-1">{t('partnerForm.company')} *</label>
          <input name="company" value={form.company} onChange={handleChange} className={`w-full border rounded px-3 py-2${errors.company ? ' border-red-500' : ''}`} />
          {errors.company && <div className="text-red-500 text-sm mt-1 flex items-center"><svg className="w-5 h-5 mr-1 text-red-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" strokeWidth="2"/><line x1="12" y1="8" x2="12" y2="12" strokeWidth="2"/><circle cx="12" cy="16" r="1" strokeWidth="2"/></svg>{errors.company}</div>}
        </div>
        <div>
          <label className="block font-semibold mb-1">{t('partnerForm.email')} *</label>
          <input name="email" type="email" value={form.email} onChange={handleChange} className={`w-full border rounded px-3 py-2${errors.email ? ' border-red-500' : ''}`} placeholder="john.doe@email.com" />
          {errors.email && <div className="text-red-500 text-sm mt-1 flex items-center"><svg className="w-5 h-5 mr-1 text-red-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" strokeWidth="2"/><line x1="12" y1="8" x2="12" y2="12" strokeWidth="2"/><circle cx="12" cy="16" r="1" strokeWidth="2"/></svg>{errors.email}</div>}
        </div>
        <div>
          <label className="block font-semibold mb-1">{t('partnerForm.phone')}</label>
          <input name="phone" value={form.phone} onChange={handleChange} className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-semibold mb-1">{t('partnerForm.location')}</label>
          <input name="location" value={form.location} onChange={handleChange} className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-semibold mb-1">{t('partnerForm.cooperationType')}</label>
          <select name="cooperationType" value={form.cooperationType} onChange={handleChange} className={`w-full border rounded px-3 py-2${errors.cooperationType ? ' border-red-500' : ''}`}>
            <option value="">--</option>
            {cooperationOptions.map(opt => (
              <option key={opt.value} value={opt.value}>{opt.label}</option>
            ))}
          </select>
          {errors.cooperationType && <div className="text-red-500 text-sm mt-1 flex items-center"><svg className="w-5 h-5 mr-1 text-red-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" strokeWidth="2"/><line x1="12" y1="8" x2="12" y2="12" strokeWidth="2"/><circle cx="12" cy="16" r="1" strokeWidth="2"/></svg>{errors.cooperationType}</div>}
        </div>
        <div>
          <label className="block font-semibold mb-1">{t('partnerForm.productCategory')}</label>
          <input name="productCategory" value={form.productCategory} onChange={handleChange} className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-semibold mb-1">{t('partnerForm.message')}</label>
          <textarea name="message" value={form.message} onChange={handleChange} className="w-full border rounded px-3 py-2 min-h-[100px]" />
        </div>
        {submitError && <div className="text-red-500 text-sm mb-2 flex items-center"><svg className="w-5 h-5 mr-1 text-red-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" strokeWidth="2"/><line x1="12" y1="8" x2="12" y2="12" strokeWidth="2"/><circle cx="12" cy="16" r="1" strokeWidth="2"/></svg>{submitError}</div>}
        <button type="submit" className="w-full bg-blue-600 text-white font-semibold py-3 rounded hover:bg-blue-700 transition" disabled={loading}>
          {loading ? t('partnerForm.submitting') || '...' : t('partnerForm.submit')}
        </button>
      </form>
    </div>
  );
};

export default PartnerPage; 