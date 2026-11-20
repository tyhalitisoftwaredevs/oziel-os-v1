import { useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { ExternalLink, Users, NotebookTabs, HeartHandshake, CalendarDays } from 'lucide-react'
import { api, type Opportunity } from '../api/api'

export default function Impact(){
  const [tab,setTab]=useState<'opportunities'|'community'>('opportunities')
  const [typeFilter,setTypeFilter]=useState('ALL')
  const [search,setSearch]=useState('')
  const qc=useQueryClient()
  const opportunities=useQuery<Opportunity[]>({queryKey:['opportunities'],queryFn:async()=> (await api.get('/opportunities')).data})
  const impact=useQuery<any[]>({queryKey:['impact'],queryFn:async()=> (await api.get('/impact')).data})
  const update=useMutation({mutationFn:(o:{id:number,status:string})=>api.put(`/opportunities/${o.id}/status`,{status:o.status}),onSuccess:()=>qc.invalidateQueries({queryKey:['opportunities']})})
  const icons=[Users,NotebookTabs,HeartHandshake,CalendarDays]
  const filteredOpportunities=(opportunities.data||[])
    .filter(o=>typeFilter==='ALL'||o.type===typeFilter)
    .filter(o=>{
      const query=search.trim().toLowerCase()
      return !query || `${o.name} ${o.company} ${o.type}`.toLowerCase().includes(query)
    })
    .sort((a,b)=>(a.closingDate||'9999-12-31').localeCompare(b.closingDate||'9999-12-31'))
  return <div className="page"><header className="page-header"><div><p className="eyebrow">OUTCOMES &amp; PIPELINE</p><h1>Impact</h1></div></header>
    <div className="filter-bar tabs"><button className={tab==='opportunities'?'active':''} onClick={()=>setTab('opportunities')}>Opportunities Tracker</button><button className={tab==='community'?'active':''} onClick={()=>setTab('community')}>Community Impact</button></div>
    {tab==='opportunities'?<>
      <div className="opportunity-tools">
        <div className="filter-bar opportunity-filter">
          {['ALL','JOB','INTERNSHIP','LEARNERSHIP','BURSARY','SCHOLARSHIP','HACKATHON'].map(type=>(
            <button key={type} className={typeFilter===type?'active':''} onClick={()=>setTypeFilter(type)}>
              {type==='ALL'?'All':type.charAt(0)+type.slice(1).toLowerCase()}
            </button>
          ))}
        </div>
        <input className="opportunity-search" value={search} onChange={e=>setSearch(e.target.value)} placeholder="Search company or opportunity" aria-label="Search opportunities"/>
      </div>
      <p className="opportunity-count"><b>{filteredOpportunities.length}</b> opportunities • Verify deadlines on the official application page</p>
      <div className="table-wrap"><table><thead><tr><th>Opportunity</th><th>Company</th><th>Type</th><th>Closing</th><th>Status</th><th/></tr></thead><tbody>{filteredOpportunities.map(o=><tr key={o.id}><td><b>{o.name}</b></td><td>{o.company}</td><td><span className="category">{o.type}</span></td>      <td>{o.closingDate?new Date(o.closingDate).toLocaleDateString('en-ZA',{day:'2-digit',month:'short',year:'numeric'}):'Check advert'}</td>      <td><select className={`status-select ${o.status.toLowerCase()}`} value={o.status} onChange={e=>update.mutate({id:o.id,status:e.target.value})}><option>WISHLIST</option><option>APPLIED</option><option>INTERVIEW</option><option>ACCEPTED</option><option>REJECTED</option></select></td><td><a href={o.link} target="_blank" rel="noreferrer" aria-label={`Open ${o.name}`}><ExternalLink size={16}/></a></td></tr>)}</tbody></table></div>
    </>:<div className="impact-grid">{(impact.data||[]).map((x,i)=>{const Icon=icons[i%icons.length];return <article className="panel impact-card" key={x.id}><Icon/><strong>{x.value}</strong><h3>{x.label}</h3><p>{x.description}</p></article>})}</div>}
  </div>
}
