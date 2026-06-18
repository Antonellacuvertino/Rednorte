import { Bell, Contrast, RefreshCw, Rows3 } from 'lucide-react';

function SettingsPanel({ settings, onChange }) {
  const toggle = (key) => onChange({ ...settings, [key]: !settings[key] });

  return (
    <section className="settings-layout">
      <div className="settings-intro">
        <span className="section-kicker">Preferencias personales</span>
        <h2>Configuracion del panel</h2>
        <p>Estos ajustes se guardan en este navegador y se aplican inmediatamente.</p>
      </div>

      <div className="settings-list">
        <label className="setting-row">
          <span className="setting-icon"><Rows3 size={20} /></span>
          <span className="setting-copy">
            <strong>Vista compacta</strong>
            <small>Reduce espacios para revisar mas informacion a la vez.</small>
          </span>
          <input
            className="switch-input"
            type="checkbox"
            checked={settings.compactMode}
            onChange={() => toggle('compactMode')}
          />
          <span className="switch-control" aria-hidden="true" />
        </label>

        <label className="setting-row">
          <span className="setting-icon"><Bell size={20} /></span>
          <span className="setting-copy">
            <strong>Mostrar notificaciones</strong>
            <small>Incluye los avisos operativos en la vista de lista de espera.</small>
          </span>
          <input
            className="switch-input"
            type="checkbox"
            checked={settings.showNotifications}
            onChange={() => toggle('showNotifications')}
          />
          <span className="switch-control" aria-hidden="true" />
        </label>

        <label className="setting-row">
          <span className="setting-icon"><RefreshCw size={20} /></span>
          <span className="setting-copy">
            <strong>Actualizacion automatica</strong>
            <small>Refresca paneles operativos cada treinta segundos.</small>
          </span>
          <input
            className="switch-input"
            type="checkbox"
            checked={settings.autoRefresh}
            onChange={() => toggle('autoRefresh')}
          />
          <span className="switch-control" aria-hidden="true" />
        </label>

        <label className="setting-row">
          <span className="setting-icon"><Contrast size={20} /></span>
          <span className="setting-copy">
            <strong>Contraste reforzado</strong>
            <small>Aumenta bordes y contraste para mejorar la lectura clinica.</small>
          </span>
          <input
            className="switch-input"
            type="checkbox"
            checked={settings.highContrast}
            onChange={() => toggle('highContrast')}
          />
          <span className="switch-control" aria-hidden="true" />
        </label>
      </div>
    </section>
  );
}

export default SettingsPanel;
