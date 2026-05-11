export function DesktopLoginScreen() {
  return (
    <div className="overflow-hidden rounded-[32px] border border-white/10 bg-[#0D0D14] shadow-[0_30px_80px_rgba(13,13,20,0.35)]">
      <div className="grid min-h-[720px] grid-cols-[1.1fr_0.9fr]">
        <div className="flex flex-col justify-between bg-[radial-gradient(circle_at_top_left,_rgba(255,77,46,0.22),_transparent_35%),linear-gradient(180deg,_#0D0D14_0%,_#12131D_100%)] p-14">
          <div>
            <div className="inline-flex rounded-full border border-[#FF4D2E33] bg-[#FF4D2E1F] px-4 py-2 text-sm font-semibold text-[#FF8B73]">
              Hazel Gym
            </div>
          </div>
          <div className="max-w-[520px]">
            <h2 className="font-['Syne'] text-6xl font-extrabold leading-[1.02] text-white">
              Accede a tu gimnasio y sigue tu progreso.
            </h2>
            <p className="mt-6 max-w-[420px] text-lg leading-8 text-[#A7ACBA]">
              Una misma plataforma para clientes, entrenadores y administración,
              conectada con el backend actual y preparada para crecer.
            </p>
          </div>
          <div className="flex gap-3 text-sm">
            <div className="rounded-full border border-[#FF4D2E44] bg-[#FF4D2E1A] px-4 py-2 text-[#FF6B50]">
              Cliente
            </div>
            <div className="rounded-full border border-[#2266FF44] bg-[#2266FF1A] px-4 py-2 text-[#7DA5FF]">
              Entrenador
            </div>
            <div className="rounded-full border border-[#22CC6644] bg-[#22CC661A] px-4 py-2 text-[#58D68D]">
              Admin
            </div>
          </div>
        </div>

        <div className="flex items-center justify-center bg-[#11131C] p-10">
          <div className="w-full max-w-[460px] rounded-[28px] border border-white/5 bg-[#171923] p-10">
            <p className="text-sm font-medium uppercase tracking-[0.24em] text-[#6B7280]">
              Inicio de sesion
            </p>
            <h3 className="mt-4 font-['Syne'] text-4xl font-bold text-white">
              Bienvenido de vuelta
            </h3>
            <p className="mt-3 text-base text-[#9CA3AF]">
              Aqui conectaremos `/api/auth/login` y `/api/auth/me`.
            </p>

            <div className="mt-10 space-y-5">
              <label className="block">
                <span className="mb-2 block text-xs font-semibold uppercase tracking-[0.2em] text-[#6B7280]">
                  Email
                </span>
                <div className="rounded-2xl border border-white/5 bg-[#1F2230] px-5 py-4 text-base text-white/80">
                  usuario@gym.es
                </div>
              </label>

              <label className="block">
                <span className="mb-2 block text-xs font-semibold uppercase tracking-[0.2em] text-[#6B7280]">
                  Contrasena
                </span>
                <div className="rounded-2xl border border-white/5 bg-[#1F2230] px-5 py-4 text-base text-white/80">
                  ••••••••
                </div>
              </label>

              <button className="w-full rounded-2xl bg-[#FF4D2E] px-5 py-4 font-['Syne'] text-lg font-bold text-white">
                Entrar
              </button>
            </div>

            <p className="mt-6 text-center text-sm text-white/25">
              Has olvidado tu contrasena?
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
