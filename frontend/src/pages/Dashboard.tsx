import { useQuery } from '@tanstack/react-query'
import { AreaChart,Area,ResponsiveContainer,CartesianGrid,XAxis,YAxis,Tooltip } from 'recharts'
import { ArrowUpRight, GitCommit, FolderCode, Flame, ScrollText, Award } from 'lucide-react'
import { api, type Project, type BuildLog } from '../api/api'

const activity=[4,7,5,9,8,12,10,14,11,16,15,19].map((v,i)=>({week:`W${i+1}`,value:v}))
const statIcons=[FolderCode,Flame,ScrollText,Award]
export default function Dashboard(){
  const stats=useQuery({queryKey:['stats'],queryFn:async()=> (await api.get('/stats/dashboard')).data})
  const projects=useQuery<Project[]>({queryKey:['projects-featured'],queryFn:async()=> (await api.get('/projects',{params:{featured:true}})).data})
  const logs=useQuery<BuildLog[]>({queryKey:['logs-latest'],queryFn:async()=> (await api.get('/logs')).data})
  const s=stats.data||{totalProjects:0,projectsShipped:0,streakDays:0,totalLogs:0,totalCertificates:0}
  const cards=[
    ['Projects Shipped',`${s.projectsShipped}+`,'owner-reported'],['Current Streak',s.streakDays,'days'],['Logs Written',s.totalLogs,'entries'],['Certifications',`${s.totalCertificates}+`,'earned']
  ]
  const date=new Intl.DateTimeFormat('en-ZA',{timeZone:'Africa/Johannesburg',weekday:'long',day:'2-digit',month:'long',year:'numeric'}).format(new Date())
  return <div className="page">
    <header className="page-header">
      <div><p className="eyebrow">COMMAND CENTER / PERSONAL OPERATING SYSTEM</p><h1>Welcome back, Oziel</h1><p className="muted">{date}</p></div>
      <span className="status-pill"><i/> OPEN TO OPPORTUNITIES</span>
    </header>
    <section className="stats-grid">{cards.map(([label,value,note],i)=>{
      const Icon=statIcons[i];return <article className="stat-card" key={String(label)}><div className="stat-icon"><Icon size={20}/></div><div><p>{label}</p><strong>{value}</strong><span>{String(note)}</span></div></article>
    })}</section>
    <section className="dashboard-grid">
      <article className="panel now-building">
        <div className="panel-head"><div><p className="eyebrow">ACTIVE SYSTEM</p><h2>Now Building</h2></div><GitCommit size={19}/></div>
        <h3>OZIEL OS v1.0</h3><p>Production proof system with Spring Boot, React and PostgreSQL.</p>
        <div className="progress-meta"><span>Build progress</span><b>72%</b></div><div className="progress"><i style={{width:'72%'}}/></div>
        <div className="phase-row"><span><i/>Backend</span><span><i/>Frontend</span><span><i/>Deployment</span></div>
      </article>
      <article className="panel chart-panel">
        <div className="panel-head"><div><p className="eyebrow">LAST 12 WEEKS</p><h2>Proof Graph</h2></div><span className="trend">+18%</span></div>
        <div className="chart"><ResponsiveContainer width="100%" height="100%">
          <AreaChart data={activity}><defs><linearGradient id="proof" x1="0" y1="0" x2="0" y2="1"><stop offset="5%" stopColor="#00AFFF" stopOpacity={.45}/><stop offset="95%" stopColor="#00AFFF" stopOpacity={0}/></linearGradient></defs><CartesianGrid stroke="#1A2336" vertical={false}/><XAxis dataKey="week" tick={{fill:'#718096',fontSize:11}} axisLine={false} tickLine={false}/><YAxis hide/><Tooltip contentStyle={{background:'#0A0F1E',border:'1px solid #1D2A40',borderRadius:'12px'}}/><Area type="monotone" dataKey="value" stroke="#00AFFF" strokeWidth={2.5} fill="url(#proof)"/></AreaChart>
        </ResponsiveContainer></div>
      </article>
    </section>
    <section className="section">
      <div className="section-head"><div><p className="eyebrow">RECENT TRANSMISSIONS</p><h2>Latest Build Log</h2></div><a href="/log">View all <ArrowUpRight size={15}/></a></div>
      <div className="log-preview">{(logs.data||[]).slice(0,3).map(log=><article key={log.id}><time>{new Date(log.createdAt).toLocaleDateString('en-ZA',{day:'2-digit',month:'short'})}</time><div><span className="category">{log.category}</span><h3>{log.title}</h3><p>{log.content}</p></div></article>)}</div>
    </section>
    <section className="section">
      <div className="section-head"><div><p className="eyebrow">SELECTED WORK</p><h2>Featured Proof</h2></div><a href="/proof">Open proof wall <ArrowUpRight size={15}/></a></div>
      <div className="proof-grid">{(projects.data||[]).slice(0,3).map((p,i)=><article className="project-card" key={p.id}><div className={`project-visual v${i}`}><span>{p.status.replace('_',' ')}</span><b>{p.title.slice(0,1)}</b></div><div className="project-body"><h3>{p.title}</h3><p>{p.solution}</p><div className="tags">{p.techStack.slice(0,3).map(t=><span key={t}>{t}</span>)}</div><small>{p.impact}</small></div></article>)}</div>
    </section>
  </div>
}
