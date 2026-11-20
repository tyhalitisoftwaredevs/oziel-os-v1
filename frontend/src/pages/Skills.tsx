import { useQuery } from '@tanstack/react-query'
import { RadarChart,PolarGrid,PolarAngleAxis,Radar,ResponsiveContainer } from 'recharts'
import { ExternalLink } from 'lucide-react'
import { api } from '../api/api'

export default function Skills(){
  const skills=useQuery<Record<string,any[]>>({queryKey:['skills'],queryFn:async()=> (await api.get('/skills')).data})
  const groups=skills.data||{}
  const radar=Object.entries(groups).map(([category,items])=>({category:category.replace('_TOOLS',''),score:Math.round(items.reduce((n,s)=>n+s.confidence,0)/Math.max(items.length,1))}))
  return <div className="page"><header className="page-header"><div><p className="eyebrow">CAPABILITY MATRIX</p><h1>Skills</h1></div></header>
    <div className="skills-layout"><section>{Object.entries(groups).map(([category,items])=><article className="panel skill-group" key={category}><h2>{category.replace('_',' ')}</h2>{items.map(s=><div className="skill-row" key={s.id}><div><b>{s.name}</b><span>{s.level}</span></div><div className="skill-progress"><i style={{width:`${s.confidence}%`}}/></div><strong>{s.confidence}%</strong>{s.proofUrl&&<a href={s.proofUrl} aria-label={`Proof for ${s.name}`}><ExternalLink size={14}/></a>}</div>)}</article>)}</section>
      <aside className="panel radar-panel"><p className="eyebrow">OVERALL SIGNAL</p><h2>Skill Radar</h2><div className="radar"><ResponsiveContainer width="100%" height="100%"><RadarChart data={radar}><PolarGrid stroke="#26344d"/><PolarAngleAxis dataKey="category" tick={{fill:'#8B98AD',fontSize:11}}/><Radar dataKey="score" stroke="#00AFFF" fill="#00AFFF" fillOpacity={.25}/></RadarChart></ResponsiveContainer></div></aside>
    </div>
  </div>
}
