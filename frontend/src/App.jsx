import { useEffect, useMemo, useState } from 'react';
import {
  CalendarDays,
  ClipboardList,
  FileBarChart,
  LogOut,
  Settings,
  Stethoscope,
  UsersRound
} from 'lucide-react';
import {
  createPatient,
  fetchPatientWithCitas,
  fetchPatients,
  setAuthToken
} from './hooks/usePatientApi';
import AppointmentForm from './components/AppointmentForm';
import CitasPublic from './components/CitasPublic';
import Login from './components/Login';
import MicroservicesPanel from './components/MicroservicesPanel';
import PatientDetail from './components/PatientDetail';
import PatientForm from './components/PatientForm';
import PatientList from './components/PatientList';
import ReassignmentPanel from './components/ReassignmentPanel';
import SettingsPanel from './components/SettingsPanel';
import logo from './assets/hospital-red-norte-logo.svg';

const AUTH_USER_KEY = 'rednorte-user';
const SETTINGS_KEY = 'rednorte-settings';
const defaultSettings = {
  compactMode: false,
  showNotifications: true,
  autoRefresh: false,
  highContrast: false
};

const pages = [
  { id: 'citas', label: 'Citas', icon: CalendarDays },
  { id: 'pacientes', label: 'Pacientes', icon: UsersRound },
  { id: 'lista-espera', label: 'Lista de espera', icon: ClipboardList },
  { id: 'reportes', label: 'Reportes', icon: FileBarChart },
  { id: 'configuracion', label: 'Configuracion', icon: Settings }
];

const pageMeta = {
  citas: ['Agenda clinica', 'Citas y reprogramaciones del equipo medico.'],
  pacientes: ['Pacientes', 'Fichas clinicas, historial y nuevas admisiones.'],
  'lista-espera': ['Operacion hospitalaria', 'Lista priorizada, avisos y trazabilidad del sistema.'],
  reportes: ['Reportes', 'Resumen de actividad para seguimiento asistencial.'],
  configuracion: ['Configuracion', 'Preferencias personales del panel clinico.']
};

function App() {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem(AUTH_USER_KEY);
    const storedUser = stored ? JSON.parse(stored) : null;
    if (storedUser?.token) setAuthToken(storedUser.token);
    return storedUser;
  });
  const [activePage, setActivePage] = useState('pacientes');
  const [patients, setPatients] = useState([]);
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [citas, setCitas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [agendaVersion, setAgendaVersion] = useState(0);
  const [settings, setSettings] = useState(() => {
    const stored = localStorage.getItem(SETTINGS_KEY);
    return stored ? { ...defaultSettings, ...JSON.parse(stored) } : defaultSettings;
  });

  useEffect(() => {
    localStorage.setItem(SETTINGS_KEY, JSON.stringify(settings));
  }, [settings]);

  const loadPatients = async () => {
    setLoading(true);
    try {
      const data = await fetchPatients();
      setPatients(data || []);
      setError('');
    } catch {
      setError('No se pudo cargar la lista de pacientes.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user && activePage === 'pacientes') loadPatients();
  }, [activePage, user]);

  const filteredPatients = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();
    if (!term) return patients;
    return patients.filter((patient) => {
      const fullName = `${patient.nombre} ${patient.apellido}`.toLowerCase();
      return fullName.includes(term) || patient.rut?.toLowerCase().includes(term);
    });
  }, [patients, searchTerm]);

  const handleLogin = (userData) => {
    const authUser = { role: 'medico', name: 'Medico RedSalud', ...userData };
    setAuthToken(authUser.token);
    setUser(authUser);
    localStorage.setItem(AUTH_USER_KEY, JSON.stringify(authUser));
  };

  const handleLogout = () => {
    setUser(null);
    setAuthToken(null);
    localStorage.removeItem(AUTH_USER_KEY);
    setActivePage('pacientes');
  };

  const handleSelectPatient = async (patientId) => {
    setLoading(true);
    try {
      const data = await fetchPatientWithCitas(patientId);
      setSelectedPatient(data.paciente);
      setCitas(data.citas || []);
      setError('');
    } catch {
      setError('No se pudieron cargar los detalles del paciente.');
    } finally {
      setLoading(false);
    }
  };

  const handleCreatePatient = async (patient) => {
    setSaving(true);
    try {
      const created = await createPatient(patient);
      await loadPatients();
      await handleSelectPatient(created.id);
      setSuccess('Paciente agregado correctamente.');
      setError('');
    } catch {
      setError('No se pudo agregar el paciente.');
    } finally {
      setSaving(false);
    }
  };

  if (!user) return <Login onLogin={handleLogin} error="" />;

  return (
    <div className={`app-shell ${settings.compactMode ? 'compact-mode' : ''} ${settings.highContrast ? 'high-contrast' : ''}`}>
      <aside className="sidebar">
        <div className="brand">
          <img src={logo} alt="Hospital Red Norte" className="brand-logo" />
          <div>
            <strong>Hospital Red Norte</strong>
            <span>Gestion clinica</span>
          </div>
        </div>

        <nav className="nav-menu" aria-label="Navegacion principal">
          {pages.map(({ id, label, icon: Icon }) => (
            <button
              className={`nav-item ${activePage === id ? 'active' : ''}`}
              type="button"
              key={id}
              onClick={() => {
                setActivePage(id);
                setError('');
                setSuccess('');
              }}
            >
              <Icon size={18} />
              <span>{label}</span>
            </button>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div className="user-summary">
            <span className="user-avatar"><Stethoscope size={18} /></span>
            <span>
              <strong>{user.name}</strong>
              <small>Sesion medica activa</small>
            </span>
          </div>
          <button className="logout-button" type="button" onClick={handleLogout}>
            <LogOut size={18} />
            <span>Cerrar sesion</span>
          </button>
        </div>
      </aside>

      <div className="workspace">
        <header className="topbar">
          <div>
            <span className="eyebrow">Hospital Red Norte</span>
            <h1>{pageMeta[activePage][0]}</h1>
            <p>{pageMeta[activePage][1]}</p>
          </div>
        </header>

        {activePage === 'citas' ? (
          <>
            <section className="schedule-grid">
              <CitasPublic refreshKey={agendaVersion} />
              <section className="panel">
                <div className="panel-header">
                  <div>
                    <span className="section-kicker">Nuevo bloque</span>
                    <h2>Agendar cita</h2>
                  </div>
                </div>
                <AppointmentForm
                  onAppointmentCreated={() => setAgendaVersion((version) => version + 1)}
                />
              </section>
            </section>
            <ReassignmentPanel
              doctorName={user.name}
              onReassigned={() => setAgendaVersion((version) => version + 1)}
            />
          </>
        ) : activePage === 'pacientes' ? (
          <>
            {(error || success) && <div className={error ? 'notice error' : 'notice success'}>{error || success}</div>}
            <section className="metrics-grid">
              <article className="metric-card"><span>Pacientes activos</span><strong>{patients.length}</strong></article>
              <article className="metric-card"><span>Citas seleccionadas</span><strong>{citas.length}</strong></article>
              <article className="metric-card"><span>Estado del sistema</span><strong className="metric-status">Operativo</strong></article>
            </section>
            <main className="dashboard-grid">
              <section className="panel patient-panel">
                <div className="panel-header">
                  <div><span className="section-kicker">Registro clinico</span><h2>Pacientes</h2></div>
                  {loading && <span className="loading-pill">Actualizando</span>}
                </div>
                <input
                  className="search-input"
                  value={searchTerm}
                  onChange={(event) => setSearchTerm(event.target.value)}
                  placeholder="Buscar por nombre o RUT"
                />
                <PatientList patients={filteredPatients} onSelect={handleSelectPatient} selectedId={selectedPatient?.id} />
              </section>
              <section className="panel detail-panel">
                <div className="panel-header"><div><span className="section-kicker">Ficha rapida</span><h2>Detalle clinico</h2></div></div>
                <PatientDetail patient={selectedPatient} citas={citas} />
              </section>
              <section className="panel form-panel">
                <div className="panel-header"><div><span className="section-kicker">Nueva admision</span><h2>Agregar paciente</h2></div></div>
                <PatientForm onSubmit={handleCreatePatient} saving={saving} />
              </section>
            </main>
          </>
        ) : activePage === 'lista-espera' ? (
          <MicroservicesPanel showNotifications={settings.showNotifications} autoRefresh={settings.autoRefresh} />
        ) : activePage === 'reportes' ? (
          <section className="report-band">
            <div className="settings-intro">
              <span className="section-kicker">Actividad asistencial</span>
              <h2>Resumen del turno</h2>
              <p>Indicadores rapidos del trabajo realizado en el panel.</p>
            </div>
            <div className="metrics-grid">
              <article className="metric-card"><span>Total pacientes</span><strong>{patients.length}</strong></article>
              <article className="metric-card"><span>Citas consultadas</span><strong>{citas.length}</strong></article>
              <article className="metric-card"><span>Perfil activo</span><strong className="metric-status">Medico</strong></article>
            </div>
          </section>
        ) : (
          <SettingsPanel settings={settings} onChange={setSettings} />
        )}
      </div>
    </div>
  );
}

export default App;
