import { PhoneFrame } from './PhoneFrame';

export function QRSuccessClase() {
  return (
    <PhoneFrame statusBarColor="#ffffff55" size="flow">
      <div className="bg-[#0A0F20] min-h-[360px] px-3.5 py-3.5 text-center">
        <div className="h-1.5"></div>
        <div className="w-[52px] h-[52px] bg-[#2266FF22] border-[1.5px] border-[#2266FF44] rounded-full flex items-center justify-center mx-auto mb-2.5">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
            <path d="M5 12l5 5L20 7" stroke="#2266FF" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        </div>
        <div className="font-['Syne'] text-[14px] font-extrabold text-white mb-1">Clase registrada</div>
        <p className="text-[9px] text-[#ffffff33] mb-3">Sesión confirmada correctamente</p>

        <div className="bg-[#1A2040] border-[0.5px] border-[#2266FF33] rounded-xl px-3 py-2.5 mb-2 text-left">
          <div className="flex justify-between items-center py-0.5">
            <span className="text-[9px] text-[#ffffff44]">Clase</span>
            <span className="text-[10px] text-white font-medium">Spinning</span>
          </div>
          <div className="flex justify-between items-center py-0.5">
            <span className="text-[9px] text-[#ffffff44]">Entrenador</span>
            <span className="text-[10px] text-white font-medium">Laura R.</span>
          </div>
          <div className="flex justify-between items-center py-0.5">
            <span className="text-[9px] text-[#ffffff44]">Inicio</span>
            <span className="text-[10px] text-white font-medium">08:30</span>
          </div>
          <div className="flex justify-between items-center py-0.5">
            <span className="text-[9px] text-[#ffffff44]">Duración</span>
            <span className="text-[10px] text-white font-medium">50 min</span>
          </div>
        </div>

        <div className="bg-[#1A2040] border-[0.5px] border-[#2266FF22] rounded-xl px-3 py-2.5 mb-2 text-left">
          <div className="text-[9px] text-[#ffffff44] mb-1.5">Clases este mes</div>
          <div className="bg-[#0A0F20] rounded-full h-1.5 overflow-hidden">
            <div className="bg-[#2266FF] h-1.5 w-[73%]"></div>
          </div>
          <div className="flex justify-between mt-1">
            <span className="text-[8px] text-[#ffffff33]">22 impartidas</span>
            <span className="text-[8px] text-[#ffffff33]">objetivo: 30</span>
          </div>
        </div>

        <button className="w-full bg-[#2266FF] rounded-[10px] py-2.5 text-center font-['Syne'] text-[11px] font-bold text-white">
          Ver agenda del día
        </button>
      </div>
    </PhoneFrame>
  );
}
