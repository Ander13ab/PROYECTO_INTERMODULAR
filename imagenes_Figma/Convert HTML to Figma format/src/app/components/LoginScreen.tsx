import { PhoneFrame } from './PhoneFrame';

export function LoginScreen() {
  return (
    <PhoneFrame statusBarColor="#ffffff88">
      <div className="bg-[#0D0D14] min-h-[360px] px-[18px] pb-[18px]">
        <div className="w-[42px] h-[42px] bg-[#FF4D2E] rounded-xl flex items-center justify-center mb-4">
          <svg viewBox="0 0 24 24" fill="none" className="w-[22px] h-[22px]">
            <path d="M4 6h16M4 12h10M4 18h7" stroke="#fff" strokeWidth="2" strokeLinecap="round"/>
            <circle cx="18" cy="17" r="3.5" stroke="#fff" strokeWidth="1.5"/>
            <path d="M21 20.5l1.5 1.5" stroke="#fff" strokeWidth="1.5" strokeLinecap="round"/>
          </svg>
        </div>

        <h1 className="font-['Syne'] text-[22px] font-extrabold text-white leading-tight mb-1">
          Bienvenido<br />de vuelta
        </h1>
        <p className="text-xs text-[#ffffff55] mb-6">Accede a tu gimnasio</p>

        <div className="bg-[#1A1A26] rounded-[10px] px-3 py-2.5 mb-2.5">
          <div className="text-[9px] text-[#ffffff44] uppercase tracking-wide mb-1">Email</div>
          <div className="text-[13px] text-[#ffffffcc]">usuario@gym.es</div>
        </div>

        <div className="bg-[#1A1A26] rounded-[10px] px-3 py-2.5 mb-2.5">
          <div className="text-[9px] text-[#ffffff44] uppercase tracking-wide mb-1">Contraseña</div>
          <div className="text-[13px] text-[#ffffffcc]">••••••••</div>
        </div>

        <button className="w-full bg-[#FF4D2E] rounded-[10px] py-3 text-center font-['Syne'] text-[13px] font-bold text-white mt-3.5 tracking-wide">
          Entrar
        </button>

        <p className="text-center text-[11px] text-[#ffffff33] mt-2.5">¿Olvidaste tu contraseña?</p>

        <p className="text-[9px] text-[#ffffff22] text-center mt-2.5 mb-1.5">Acceso según rol</p>
        <div className="flex gap-1.5">
          <div className="flex-1 text-center py-1.5 rounded-lg text-[9px] font-medium tracking-wide bg-[#FF4D2E22] text-[#FF6B50] border-[0.5px] border-[#FF4D2E44]">
            Cliente
          </div>
          <div className="flex-1 text-center py-1.5 rounded-lg text-[9px] font-medium tracking-wide bg-[#2266FF22] text-[#5588FF] border-[0.5px] border-[#2266FF44]">
            Entrenador
          </div>
          <div className="flex-1 text-center py-1.5 rounded-lg text-[9px] font-medium tracking-wide bg-[#22CC8822] text-[#44DD99] border-[0.5px] border-[#22CC8844]">
            Admin
          </div>
        </div>
      </div>
    </PhoneFrame>
  );
}
