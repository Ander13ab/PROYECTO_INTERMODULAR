import { PhoneFrame } from './PhoneFrame';

interface QRScannerScreenProps {
  variant?: 'default' | 'flow';
}

export function QRScannerScreen({ variant = 'default' }: QRScannerScreenProps) {
  const isFlow = variant === 'flow';
  const padding = isFlow ? 'px-3.5' : 'px-[18px]';
  const viewfinderHeight = isFlow ? 'h-[155px]' : 'h-[170px]';

  return (
    <PhoneFrame statusBarColor="#ffffff66" size={isFlow ? 'flow' : 'default'}>
      <div className="bg-[#0D0D14] min-h-[360px]">
        <div className={`py-2.5 flex items-center gap-2 ${padding}`}>
          <div className="w-7 h-7 bg-[#1A1A26] rounded-lg flex items-center justify-center">
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none">
              <path d="M15 19l-7-7 7-7" stroke="#fff" strokeWidth="2" strokeLinecap="round"/>
            </svg>
          </div>
          <span className="font-['Syne'] text-[14px] font-bold text-white">Escanear QR</span>
        </div>

        <div className={`flex gap-1.5 mb-2.5 ${padding}`}>
          <div className="flex-1 text-center py-1.5 rounded-[10px] text-[10px] font-medium bg-[#FF4D2E] text-white">
            Entrada
          </div>
          <div className="flex-1 text-center py-1.5 rounded-[10px] text-[10px] font-medium bg-[#1A1A26] text-[#ffffff55]">
            Máquina
          </div>
          <div className="flex-1 text-center py-1.5 rounded-[10px] text-[10px] font-medium bg-[#1A1A26] text-[#ffffff55]">
            Clase
          </div>
        </div>

        <div className={`bg-[#1A1A26] rounded-[18px] ${viewfinderHeight} flex items-center justify-center relative overflow-hidden ${padding} mx-[18px]`}>
          <div className="absolute top-3 left-3 w-6 h-6 border-[#FF4D2E] border-l-[2.5px] border-t-[2.5px] rounded-tl"></div>
          <div className="absolute top-3 right-3 w-6 h-6 border-[#FF4D2E] border-r-[2.5px] border-t-[2.5px] rounded-tr"></div>
          <div className="absolute bottom-3 left-3 w-6 h-6 border-[#FF4D2E] border-l-[2.5px] border-b-[2.5px] rounded-bl"></div>
          <div className="absolute bottom-3 right-3 w-6 h-6 border-[#FF4D2E] border-r-[2.5px] border-b-[2.5px] rounded-br"></div>
          <div className="absolute w-[70%] h-[1.5px] bg-gradient-to-r from-transparent via-[#FF4D2E88] to-transparent"></div>
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none">
            <rect x="3" y="3" width="7" height="7" rx="1" stroke="#ffffff33" strokeWidth="1.5"/>
            <rect x="14" y="3" width="7" height="7" rx="1" stroke="#ffffff33" strokeWidth="1.5"/>
            <rect x="3" y="14" width="7" height="7" rx="1" stroke="#ffffff33" strokeWidth="1.5"/>
            <rect x="5" y="5" width="3" height="3" fill="#ffffff33"/>
            <rect x="16" y="5" width="3" height="3" fill="#ffffff33"/>
            <rect x="5" y="16" width="3" height="3" fill="#ffffff33"/>
            <path d="M14 14h2v2h-2zM16 16h2v2h-2zM18 14h2v2h-2zM14 18h2v2h-2zM18 18h2v2h-2z" fill="#ffffff33"/>
          </svg>
        </div>

        {isFlow && (
          <p className="text-[9px] text-[#ffffff33] text-center mt-2 px-3.5">
            Apunta al código QR para registrar tu entrada
          </p>
        )}

        <div className={`flex gap-1.5 mt-3 mb-2.5 ${padding}`}>
          <div className="flex-1 bg-[#1A1A26] rounded-[10px] px-1.5 py-2 text-center">
            <div className="font-['Syne'] text-[15px] font-bold text-white">47</div>
            <div className="text-[8px] text-[#ffffff33]">Visitas</div>
          </div>
          <div className="flex-1 bg-[#1A1A26] rounded-[10px] px-1.5 py-2 text-center">
            <div className="font-['Syne'] text-[15px] font-bold text-white">14</div>
            <div className="text-[8px] text-[#ffffff33]">Racha</div>
          </div>
          <div className="flex-1 bg-[#1A1A26] rounded-[10px] px-1.5 py-2 text-center">
            <div className="font-['Syne'] text-[15px] font-bold text-white">3</div>
            <div className="text-[8px] text-[#ffffff33]">Logros</div>
          </div>
        </div>

        {!isFlow && (
          <div className={`bg-[#1A1A26] rounded-[10px] py-2 text-center text-[9px] text-[#ffffff44] flex items-center justify-center gap-1.5 ${padding}`}>
            <svg width="11" height="11" viewBox="0 0 24 24" fill="none">
              <path d="M9 2h6l1 7H8L9 2z" stroke="#ffffff44" strokeWidth="1.5"/>
              <path d="M8 9l-1 13h10L16 9" stroke="#ffffff44" strokeWidth="1.5"/>
              <path d="M12 12v6" stroke="#ffffff44" strokeWidth="1.5" strokeLinecap="round"/>
            </svg>
            <span>Linterna</span>
          </div>
        )}
      </div>
    </PhoneFrame>
  );
}
