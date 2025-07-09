import React from 'react';

interface LogoProps {
  className?: string;
  size?: 'small' | 'normal';
}

const Logo: React.FC<LogoProps> = ({ className = '', size = 'normal' }) => {
  const iconSize = size === 'small' ? 'w-6 h-6' : 'w-8 h-8';
  const textSize = size === 'small' ? 'text-lg' : 'text-xl';
  
  return (
    <div className={`flex items-center ${className}`}>
      <svg 
        className={`${iconSize} text-pink-500`}
        viewBox="0 0 24 24" 
        fill="none" 
        stroke="currentColor" 
        strokeWidth="2" 
        strokeLinecap="round" 
        strokeLinejoin="round"
      >
        <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path>
        <line x1="3" y1="6" x2="21" y2="6"></line>
        <path d="M16 10a4 4 0 0 1-8 0"></path>
      </svg>
      <span className={`ml-2 ${textSize} font-bold`}>Celestica Shop</span>
    </div>
  );
};

export default Logo; 