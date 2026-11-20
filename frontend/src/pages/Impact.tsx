import { useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { ExternalLink, Users, NotebookTabs, HeartHandshake, CalendarDays, GraduationCap } from 'lucide-react'
import { api, type Opportunity } from '../api/api'

const postgraduatePathways = [
  {
    university: 'University of Pretoria',
    programme: 'BSc Hons in Computer Science',
    guidance: 'UP lists this Honours programme. Ask the department to assess your TUT qualification, Computer Science and Mathematics modules, and the current intake requirements.',
    links: [{ label: 'Computer Science postgraduate programmes', url: 'https://www.up.ac.za/computer-science/postgraduate-degrees' }],
  },
  {
    university: 'University of the Witwatersrand',
    programme: 'BSc Honours in Computer Science',
    guidance: 'Wits lists an equivalent NQF 7 degree with a Computer Science major, second-year university Mathematics, programming proficiency, and a 70% aggregate for consideration. Selection is limited; confirm TUT equivalence.',
    links: [{ label: 'Programme details and entry requirements', url: 'https://www.wits.ac.za/course-finder/postgraduate/science/computer-science/' }],
  },
  {
    university: 'University of Johannesburg',
    programme: 'Computer Science Honours options',
    guidance: 'UJ lists an NQF 7 degree with at least 65% in Computer Science; a practical programming assessment may apply. The 2026 page listed a 30 September closing date, so check the next application cycle.',
    links: [
      { label: 'BScHons in Computer Science', url: 'https://www.uj.ac.za/university-courses/bschons-in-computer-science/' },
      { label: 'BScHons in Computer Science with AI', url: 'https://www.uj.ac.za/university-courses/bschons-in-computer-science-with-ai/' },
      { label: 'BScHons in Computer Science with Cyber Security', url: 'https://www.uj.ac.za/university-courses/bschons-in-comp-sc-with-cyber-security/' },
    ],
  },
]

const advancedDiplomaPathways = [
  {
    university: 'University of Johannesburg',
    programme: 'Advanced Diploma in Business Information Technology',
    guidance: 'UJ lists an appropriate bachelor’s degree or a three-year Diploma in IT, at least one year of software-development experience, and a 60% final-year average. Its 2026 application deadline was 30 September; confirm the next cycle and that your TUT qualification is accepted.',
    links: [{ label: 'Programme details and entry requirements', url: 'https://www.uj.ac.za/university-courses/advanced-diploma-in-business-information-technology/' }],
  },
  {
    university: 'Tshwane University of Technology',
    programme: 'Advanced Diploma in Computer Science or Information Technology',
    guidance: 'TUT lists both programmes at NQF Level 7. This is a route at your current institution, not proof that external applicants are accepted; ask the faculty about entry requirements and recognition of your exact qualification.',
    links: [
      { label: 'Computer Science programme list', url: 'https://www.tut.ac.za/ict/computer-science/' },
      { label: 'Information Technology programme list', url: 'https://www.tut.ac.za/ict/information-technology/' },
    ],
  },
]

function getOpportunitySource(link: string) {
  const host = /^https?:\/\/([^/:?#]+)/i.exec(link.trim())?.[1]?.toLowerCase()
  if (!host) return 'External link'
  if (host === 'linkedin.com' || host.endsWith('.linkedin.com')) return 'LinkedIn'
  if (host === 'indeed.com' || host.endsWith('.indeed.com') || host === 'indeed.co.za' || host.endsWith('.indeed.co.za')) return 'Indeed'
  return 'External link'
}

export default function Impact(){
  const [tab,setTab]=useState<'opportunities'|'community'|'postgraduate'>('opportunities')
  const [pathwayLevel,setPathwayLevel]=useState<'honours'|'advanced-diploma'>('honours')
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
    <div className="filter-bar tabs"><button className={tab==='opportunities'?'active':''} onClick={()=>setTab('opportunities')}>Opportunities Tracker</button><button className={tab==='postgraduate'?'active':''} onClick={()=>setTab('postgraduate')}>Postgraduate Pathways</button><button className={tab==='community'?'active':''} onClick={()=>setTab('community')}>Community Impact</button></div>
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
      <div className="table-wrap">
        <table>
          <thead><tr><th>Opportunity</th><th>Company</th><th>Type</th><th>Closing</th><th>Status</th><th /></tr></thead>
          <tbody>
            {filteredOpportunities.map(o=>(
              <tr key={o.id}>
                <td><b>{o.name}</b><small className="opportunity-source">{getOpportunitySource(o.link)}</small></td>
                <td>{o.company}</td>
                <td><span className="category">{o.type}</span></td>
                <td>{o.closingDate?new Date(o.closingDate).toLocaleDateString('en-ZA',{day:'2-digit',month:'short',year:'numeric'}):'Check advert'}</td>
                <td><select className={`status-select ${o.status.toLowerCase()}`} value={o.status} onChange={e=>update.mutate({id:o.id,status:e.target.value})}><option>WISHLIST</option><option>APPLIED</option><option>INTERVIEW</option><option>ACCEPTED</option><option>REJECTED</option></select></td>
                <td><a href={o.link} target="_blank" rel="noreferrer" aria-label={`Open ${o.name}`}><ExternalLink size={16}/></a></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>:tab==='postgraduate'?<>
      <div className="filter-bar tabs">
        <button className={pathwayLevel==='honours'?'active':''} onClick={()=>setPathwayLevel('honours')}>Honours</button>
        <button className={pathwayLevel==='advanced-diploma'?'active':''} onClick={()=>setPathwayLevel('advanced-diploma')}>Advanced Diploma</button>
      </div>
      <p className="postgraduate-note">{pathwayLevel==='honours'
        ? 'Honours is a separate study route from an Advanced Diploma. Ask each university to assess your completed TUT qualification, NQF level, marks and required modules; admission is not guaranteed.'
        : 'Advanced Diplomas are a separate route from Honours. The listed programmes have their own prior-qualification and experience requirements; confirm that your TUT award is accepted and check the next application cycle.'}</p>
      <div className="impact-grid postgraduate-grid">
        {(pathwayLevel==='honours'?postgraduatePathways:advancedDiplomaPathways).map(pathway=>(
          <article className="panel postgraduate-card" key={pathway.university}>
            <GraduationCap size={22}/>
            <p className="eyebrow">{pathwayLevel==='honours'?'HONOURS PATHWAY':'ADVANCED DIPLOMA PATHWAY'}</p>
            <h2>{pathway.university}</h2>
            <h3>{pathway.programme}</h3>
            <p>{pathway.guidance}</p>
            <div className="postgraduate-programs">
              {pathway.links.map(link=>(
                <a key={link.url} href={link.url} target="_blank" rel="noreferrer">
                  {link.label}<ExternalLink size={14}/>
                </a>
              ))}
            </div>
          </article>
        ))}
      </div>
    </>:<div className="impact-grid">{(impact.data||[]).map((x,i)=>{const Icon=icons[i%icons.length];return <article className="panel impact-card" key={x.id}><Icon/><strong>{x.value}</strong><h3>{x.label}</h3><p>{x.description}</p></article>})}</div>}
  </div>
}
