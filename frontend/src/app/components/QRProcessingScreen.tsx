import { PhoneFrame } from './PhoneFrame';

export function QRProcessingScreen() {
  return (
    <PhoneFrame statusBarColor="#ffffff55" size="flow">
      <div className="bg-[#0D0D14] min-h-[360px] px-4 py-6 text-center">
        <div className="h-5"></div>
        <div className="w-[52px] h-[52px] border-[2.5px] border-[#1C1C28] border-t-[#FF4D2E] rounded-full mx-auto mb-4 animate-spin"></div>
        <div className="font-['Syne'] text-[14px] font-bold text-white mb-1.5">Verificando QR</div>
        <p className="text-[10px] text-[#ffffff33] mb-5">Comprobando datos del usuario...</p>

        <div className="flex justify-center gap-1.5 mb-6">
          <div className="w-1.5 h-1.5 rounded-full bg-[#FF4D2E]"></div>
          <div className="w-1.5 h-1.5 rounded-full bg-[#FF4D2E88]"></div>
          <div className="w-1.5 h-1.5 rounded-full bg-[#FF4D2E33]"></div>
        </div>

        <div className="bg-[#1C1C28] rounded-xl px-3 py-2.5 flex items-center gap-2 text-left">
          <div className="w-[30px] h-[30px] bg-[#FF4D2E22] rounded-lg flex items-center justify-center flex-shrink-0">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
              <rect x="3" y="3" width="7" height="7" rx="1" stroke="#FF4D2E" strokeWidth="1.5"/>
              <rect x="14" y="3" width="7" height="7" rx="1" stroke="#FF4D2E" strokeWidth="1.5"/>
              <rect x="3" y="14" width="7" height="7" rx="1" stroke="#FF4D2E" strokeWidth="1.5"/>
              <rect x="5" y="5" width="3" height="3" fill="#FF4D2E"/>
              <rect x="16" y="5" width="3" height="3" fill="#FF4D2E"/>
              <rect x="5" y="16" width="3" height="3" fill="#FF4D2E"/>
            </svg>
          </div>
          <div>
            <div className="text-[10px] text-[#ffffff88]">QR de entrada detectado</div>
            <div className="text-[9px] text-[#ffffff33]">Hazel Gym · Sede principal</div>
          </div>
        </div>

        <div className="h-[30px]"></div>
        <p className="text-[9px] text-[#ffffff22]">No cierres la aplicación</p>
      </div>
    </PhoneFrame>
  );
}
