import { PhoneFrame } from './PhoneFrame';

export function QRSuccessEntrada() {
  return (
    <PhoneFrame statusBarColor="#ffffff55" size="flow">
      <div className="bg-[#0A1A0E] min-h-[360px] px-4 py-[18px] text-center">
        <div className="h-2"></div>
        <div className="w-14 h-14 bg-[#22CC6622] border-[1.5px] border-[#22CC6644] rounded-full flex items-center justify-center mx-auto mb-3">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M5 12l5 5L20 7" stroke="#22CC66" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        </div>
        <div className="font-['Syne'] text-[15px] font-extrabold text-white mb-1">¡Entrada registrada!</div>
        <p className="text-[10px] text-[#ffffff44] mb-3.5">Bienvenido a Hazel Gym</p>

        <div className="bg-[#1A2E1E] border-[0.5px] border-[#22CC6633] rounded-xl px-3 py-2.5 mb-2.5 text-left">
          <div className="flex justify-between items-center py-1">
            <span className="text-[9px] text-[#ffffff44]">Usuario</span>
            <span className="text-[10px] text-white font-medium">Carlos M.</span>
          </div>
          <div className="flex justify-between items-center py-1">
            <span className="text-[9px] text-[#ffffff44]">Hora</span>
            <span className="text-[10px] text-white font-medium">09:41</span>
          </div>
          <div className="flex justify-between items-center py-1">
            <span className="text-[9px] text-[#ffffff44]">Fecha</span>
            <span className="text-[10px] text-white font-medium">19 abr 2026</span>
          </div>
          <div className="flex justify-between items-center py-1">
            <span className="text-[9px] text-[#ffffff44]">Visita nº</span>
            <span className="text-[10px] text-[#22CC66] font-medium">48</span>
          </div>
        </div>

        <div className="inline-flex items-center gap-1 bg-[#FF4D2E22] border-[0.5px] border-[#FF4D2E44] rounded-full px-2 py-1">
          <span className="text-[9px] text-[#FF6B50] font-medium">🔥 Racha: 15 días seguidos</span>
        </div>

        <div className="h-2.5"></div>
        <button className="w-full bg-[#22CC66] rounded-[10px] py-2.5 text-center font-['Syne'] text-[11px] font-bold text-[#0A1A0E]">
          Volver al inicio
        </button>
      </div>
    </PhoneFrame>
  );
}
