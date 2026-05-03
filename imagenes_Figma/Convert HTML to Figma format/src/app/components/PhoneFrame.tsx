import { ReactNode } from 'react';

interface PhoneFrameProps {
  children: ReactNode;
  statusBarColor?: string;
  size?: 'default' | 'flow';
}

export function PhoneFrame({ children, statusBarColor = '#ffffff66', size = 'default' }: PhoneFrameProps) {
  const width = size === 'flow' ? 'w-[152px]' : 'w-[175px]';
  const borderRadius = size === 'flow' ? 'rounded-[26px]' : 'rounded-[28px]';

  return (
    <div className={`${width} ${borderRadius} border-[1.5px] border-[#00000015] overflow-hidden shadow-sm`}>
      <div className="flex justify-between items-center px-3.5 pt-2.5 pb-1 text-[9px] font-medium" style={{ color: statusBarColor }}>
        <span>9:41</span>
        <div className="flex gap-0.5">
          <div className="w-1 h-1 rounded-full" style={{ background: statusBarColor }}></div>
          <div className="w-1 h-1 rounded-full" style={{ background: statusBarColor }}></div>
          <div className="w-1 h-1 rounded-full" style={{ background: statusBarColor }}></div>
        </div>
      </div>
      {children}
    </div>
  );
}
