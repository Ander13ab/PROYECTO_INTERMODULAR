function AdminSidebarItem({
  label,
  active = false,
}: {
  label: string;
  active?: boolean;
}) {
  return (
    <div
      className={`rounded-2xl px-4 py-3 text-sm ${
        active
          ? 'bg-[#22CC66] font-semibold text-[#0D2010]'
          : 'bg-[#133B21] text-[#D1FAE5]'
      }`}
    >
      {label}
    </div>
  );
}

function AdminMetric({
  label,
  value,
}: {
  label: string;
  value: string;
}) {
  return (
    <div className="rounded-[22px] border border-[#E7EAEE] bg-white p-6">
      <p className="text-sm text-[#667085]">{label}</p>
      <p className="mt-4 font-['Syne'] text-4xl font-extrabold text-[#0D2010]">
        {value}
      </p>
    </div>
  );
}

interface DesktopAdminDashboardProps {
  userName: string;
  onLogout: () => void;
}

export function DesktopAdminDashboard({
  userName,
  onLogout,
}: DesktopAdminDashboardProps) {
  return (
    <div className="overflow-hidden rounded-[32px] border border-[#E7EAEE] bg-[#F2F5F2] shadow-[0_24px_60px_rgba(15,23,42,0.10)]">
      <div className="grid min-h-[760px] grid-cols-[240px_1fr]">
        <aside className="bg-[#0D2010] px-5 py-7">
          <h2 className="font-['Syne'] text-3xl font-extrabold text-white">
            Hazel Gym
          </h2>
          <p className="mt-2 text-sm text-[#A7F3D0]">Administracion</p>
          <div className="mt-10 space-y-3">
            <AdminSidebarItem label="Dashboard" active />
            <AdminSidebarItem label="Usuarios" />
            <AdminSidebarItem label="QR" />
            <AdminSidebarItem label="Maquinas" />
            <AdminSidebarItem label="Clases" />
            <AdminSidebarItem label="Cuotas" />
          </div>
        </aside>

        <main className="p-10">
          <div className="mb-9 flex items-center justify-between gap-6">
            <div>
              <p className="text-sm text-[#667085]">Panel de administracion</p>
              <h3 className="font-['Syne'] text-5xl font-extrabold text-[#0D2010]">
                {userName}
              </h3>
            </div>
            <div className="flex items-center gap-3">
              <div className="rounded-full bg-[#DCFCE7] px-5 py-3 text-sm font-semibold text-[#166534]">
                Administrador
              </div>
              <button
                className="rounded-full bg-[#0D2010] px-5 py-3 text-sm font-semibold text-white"
                onClick={onLogout}
              >
                Cerrar sesion
              </button>
            </div>
          </div>

          <div className="grid grid-cols-4 gap-5">
            <AdminMetric label="Socios activos" value="148" />
            <AdminMetric label="Entrenadores" value="12" />
            <AdminMetric label="Maquinas" value="31" />
            <AdminMetric label="Clases hoy" value="7" />
          </div>

          <div className="mt-8 grid grid-cols-2 gap-6">
            <section className="rounded-[28px] border border-[#E7EAEE] bg-white p-7">
              <h4 className="font-['Syne'] text-2xl font-bold text-[#0D2010]">
                Gestion rapida
              </h4>
              <div className="mt-6 space-y-4">
                {[
                  ['Gestion de usuarios', 'Roles, altas y bajas'],
                  ['Generar QR', 'Entrada, maquinas y clases'],
                  ['Maquinas', 'Catalogo y estado'],
                  ['Cuotas', 'Pagos y seguimiento'],
                ].map(([title, subtitle]) => (
                  <div
                    key={title}
                    className="rounded-[20px] border border-[#EEF2F7] bg-[#F8FAFC] px-5 py-4"
                  >
                    <p className="text-lg font-semibold text-[#0D2010]">{title}</p>
                    <p className="mt-2 text-sm text-[#98A2B3]">{subtitle}</p>
                  </div>
                ))}
              </div>
            </section>

            <section className="rounded-[28px] border border-[#E7EAEE] bg-white p-7">
              <h4 className="font-['Syne'] text-2xl font-bold text-[#0D2010]">
                Actividad reciente
              </h4>
              <div className="mt-6 space-y-4">
                {[
                  ['Nuevo cliente registrado', 'Hoy · 09:14'],
                  ['QR de maquina generado', 'Hoy · 10:02'],
                  ['Clase HIIT actualizada', 'Hoy · 11:27'],
                  ['Cuota mensual registrada', 'Hoy · 12:05'],
                ].map(([title, time]) => (
                  <div
                    key={title}
                    className="rounded-[20px] border border-[#EEF2F7] bg-white px-5 py-4"
                  >
                    <p className="text-lg font-semibold text-[#0D2010]">{title}</p>
                    <p className="mt-2 text-sm text-[#98A2B3]">{time}</p>
                  </div>
                ))}
              </div>
            </section>
          </div>
        </main>
      </div>
    </div>
  );
}
