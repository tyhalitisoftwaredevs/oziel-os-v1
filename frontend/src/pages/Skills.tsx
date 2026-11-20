import { useQuery } from '@tanstack/react-query'
import { RadarChart,PolarGrid,PolarAngleAxis,Radar,ResponsiveContainer } from 'recharts'
import { ExternalLink, GraduationCap } from 'lucide-react'
import { api, type Certification } from '../api/api'

export default function Skills(){
  const skills=useQuery<Record<string,any[]>>({queryKey:['skills'],queryFn:async()=> (await api.get('/skills')).data})
  const certifications=useQuery<Certification[]>({queryKey:['certifications'],queryFn:async()=> (await api.get('/certifications')).data})
  const groups=skills.data||{}
  const radar=Object.entries(groups).map(([category,items])=>({category:category.replace('_TOOLS',''),score:Math.round(items.reduce((n,s)=>n+s.confidence,0)/Math.max(items.length,1))}))
  return <div className="page"><header className="page-header"><div><p className="eyebrow">CAPABILITY MATRIX</p><h1>Skills</h1></div></header>
    <div className="skills-layout"><section>{Object.entries(groups).map(([category,items])=><article className="panel skill-group" key={category}><h2>{category.replace('_',' ')}</h2>{items.map(s=><div className="skill-row" key={s.id}><div><b>{s.name}</b><span>{s.level}</span></div><div className="skill-progress"><i style={{width:`${s.confidence}%`}}/></div><strong>{s.confidence}%</strong>{s.proofUrl&&<a href={s.proofUrl} aria-label={`Proof for ${s.name}`}><ExternalLink size={14}/></a>}</div>)}</article>)}</section>
      <aside className="panel radar-panel"><p className="eyebrow">OVERALL SIGNAL</p><h2>Skill Radar</h2><div className="radar"><ResponsiveContainer width="100%" height="100%"><RadarChart data={radar}><PolarGrid stroke="#26344d"/><PolarAngleAxis dataKey="category" tick={{fill:'#8B98AD',fontSize:11}}/><Radar dataKey="score" stroke="#00AFFF" fill="#00AFFF" fillOpacity={.25}/></RadarChart></ResponsiveContainer></div></aside>
    </div>
    <section className="section credential-section">
      <div className="section-head"><div><p className="eyebrow">VERIFIABLE LEARNING</p><h2>Credentials</h2></div><span className="category">{certifications.data?.length||0} public</span></div>
      {certifications.isPending&&<p className="credential-empty">Loading published credentials…</p>}
      {certifications.isError&&<p className="error">Credentials could not be loaded. Please try again later.</p>}
      {certifications.isSuccess&&certifications.data.length===0&&<div className="panel credential-empty"><GraduationCap/><p>No credentials have been published yet. Add your real certificate details in Admin and choose which ones to show publicly.</p></div>}
      {certifications.data&&certifications.data.length>0&&<div className="credential-grid">{certifications.data.map(c=><article className="panel credential-card" key={c.id}>
        <span className="category">{c.category}</span>
        <h3>{c.name}</h3>
        <p>{c.issuer}</p>
        <small>Earned {new Date(`${c.awardedOn}T00:00:00`).toLocaleDateString('en-ZA',{day:'2-digit',month:'short',year:'numeric'})}</small>
        {c.expiresOn&&<small>Expires {new Date(`${c.expiresOn}T00:00:00`).toLocaleDateString('en-ZA',{day:'2-digit',month:'short',year:'numeric'})}</small>}
        {c.credentialId&&<small>Credential ID: {c.credentialId}</small>}
        {c.credentialUrl&&<a href={c.credentialUrl} target="_blank" rel="noreferrer">View issuer evidence <ExternalLink size={14}/></a>}
      </article>)}</div>}
    </section>
  </div>
}
