import { useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { ArrowLeft, ArrowUpRight, ExternalLink, Code2 } from 'lucide-react'
import { api, type Project } from '../api/api'

const filters=['All','Web','Data','Cloud','Java']
const categoryTerms:Record<string,string[]>={
  Web:['web','react','typescript','vite','javascript','frontend'],
  Data:['data','sql','postgres','postgresql','analytics','etl','recharts'],
  Cloud:['cloud','aws','azure','gcp','docker','kubernetes','devops','github actions'],
  Java:['java','spring'],
}

function matchesCategory(project:Project,filter:string){
  if(filter==='All') return true
  const terms=categoryTerms[filter]||[filter.toLowerCase()]
  return project.techStack.some(technology=>terms.some(term=>technology.toLowerCase().includes(term)))
}

export default function Proof(){
  const {slug}=useParams()
  const [filter,setFilter]=useState('All')
  const projects=useQuery<Project[]>({queryKey:['projects'],queryFn:async()=> (await api.get('/projects')).data})
  const items=projects.data||[]

  if(slug){
    if(projects.isLoading) return <div className="page"><p className="muted">Loading project brief…</p></div>
    const project=items.find(item=>item.slug===slug)
    if(!project) return <div className="page"><p className="eyebrow">PROOF WALL</p><h1>Project not found</h1><Link className="text-link" to="/proof"><ArrowLeft size={15}/> Back to projects</Link></div>
    return <div className="page">
      <header className="page-header"><div><p className="eyebrow">{project.status==='CONCEPT'?'PROJECT BRIEF':'CASE STUDY'}</p><h1>{project.title}</h1></div><span className="status-pill"><i/> {project.status.replace('_',' ')}</span></header>
      <article className="panel detail-panel"><div className="detail-visual">O.</div><div>
        <p className="eyebrow">THE PROBLEM</p><h2>{project.problem}</h2>
        <p className="eyebrow">THE APPROACH</p><p>{project.solution}</p>
        <div className="tags">{project.techStack.map(technology=><span key={technology}>{technology}</span>)}</div>
        <p className="detail-impact">{project.impact}</p>
        {project.githubUrl&&project.githubUrl!=='https://github.com'&&<a className="detail-link" href={project.githubUrl} target="_blank" rel="noreferrer"><Code2 size={15}/> View source code</a>}
      </div></article>
    </div>
  }

  const filtered=items.filter(project=>matchesCategory(project,filter))
  return <div className="page">
    <header className="page-header"><div><p className="eyebrow">PORTFOLIO SYSTEM</p><h1>Proof Wall</h1><p className="muted">Shipped work, active builds and clearly labelled project briefs.</p></div></header>
    <div className="filter-bar">{filters.map(option=><button key={option} onClick={()=>setFilter(option)} className={filter===option?'active':''}>{option}</button>)}</div>
    {projects.isLoading?<p className="muted">Loading proof…</p>:projects.isError?<p className="error">Projects could not be loaded. Please try again.</p>:filtered.length===0?
      <div className="panel empty-proof"><h2>No {filter} projects yet</h2><p>Choose another category or return to all project briefs.</p><button className="secondary" onClick={()=>setFilter('All')}>Show all proof</button></div>:
      <div className="proof-grid large">{filtered.map((project,index)=><article className="project-card" key={project.id}>
        <div className={`project-visual v${index%3}`}><span>{project.status.replace('_',' ')}</span><b>{project.title.slice(0,1)}</b></div>
        <div className="project-body"><h3>{project.title}</h3><p className="one-line">{project.problem}</p><p className="one-line">{project.solution}</p><div className="tags">{project.techStack.map(technology=><span key={technology}>{technology}</span>)}</div><small>{project.impact}</small><div className="card-actions">
          {project.demoUrl&&<a href={project.demoUrl}><ExternalLink size={14}/> Live Demo</a>}
          {project.githubUrl&&project.githubUrl!=='https://github.com'&&<a href={project.githubUrl} target="_blank" rel="noreferrer"><Code2 size={14}/> View Code</a>}
          <Link to={`/proof/${project.slug}`}>{project.demoUrl?'Project Details':'View Brief'} <ArrowUpRight size={14}/></Link>
        </div></div>
      </article>)}</div>}
  </div>
}
