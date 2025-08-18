import React from 'react';
import Logo from './Logo';
import { useTranslation } from 'react-i18next';

const Footer: React.FC = () => {
  const { t } = useTranslation();
  return (
    <footer className="bg-gray-800 text-white py-8">
      <div className="container mx-auto px-4">
        <div className="flex flex-wrap justify-between">
          <div className="w-full md:w-1/3 mb-6 md:mb-0">
            <Logo className="text-white mb-4" />
            <p className="text-gray-300">
              {t('footer.tagline')}
            </p>
          </div>
          
          <div className="w-full md:w-1/3 mb-6 md:mb-0">
            <h3 className="text-lg font-semibold mb-4">{t('footer.links')}</h3>
            <ul className="space-y-2">
              <li><a href="#" className="hover:text-blue-300 transition-colors">{t('footer.home')}</a></li>
              <li><a href="#" className="hover:text-blue-300 transition-colors">{t('footer.about')}</a></li>
              <li><a href="#" className="hover:text-blue-300 transition-colors">{t('footer.contacts')}</a></li>
            </ul>
          </div>
          
          <div className="w-full md:w-1/3">
            <h3 className="text-lg font-semibold mb-4">{t('footer.contactTitle')}</h3>
            <address className="not-italic text-gray-300">
              <p>{t('footer.address')}</p>
              <p>{t('footer.email')}</p>
            </address>
          </div>
        </div>
        
        <div className="border-t border-gray-700 mt-8 pt-6 text-center text-gray-400">
          <p>&copy; {new Date().getFullYear()} Celestica Shop. {t('footer.rights')}</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer; 