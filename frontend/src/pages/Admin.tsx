import { useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { ShieldCheck, Trash2, Plus, X } from 'lucide-react'
import { api, type Project, type BuildLog, type Opportunity } from '../api/api'

export default function Admin(){
  const [token,setToken]=useState(localStorage.getItem('oziel-token'))
  const [email,setEmail]=useState('admin@ozielos.co.za');const [password,setPassword]=useState('')
  const [error,setError]=useState('')
  const login=useMutation({mutationFn:()=>api.post('/auth/login',{email,password}),onSuccess:r=>{localStorage.setItem('oziel-token',r.data.token);setToken(r.data.token)},onError:()=>setError('Invalid admin credentials')})
  if(!token)return <div className="login-wrap"><form className="login-card panel" onSubmit={e=>{e.preventDefault();login.mutate()}}><div className="brand"><span className="brand-mark">O.</span><span>OZIEL <b>OS</b></span></div><ShieldCheck size={28}/><p className="eyebrow">AUTHORIZED SYSTEM</p><h1>Admin login</h1><label>Email<input value={email} onChange={e=>setEmail(e.target.value)} type="email"/></label><label>Password<input value={password} onChange={e=>setPassword(e.target.value)} type="password"/></label>{error&&<p className="error">{error}</p>}<button className="primary" disabled={login.isPending}>Unlock dashboard</button></form></div>
  return <AdminDashboard/>
}

function AdminDashboard(){
  const qc=useQueryClient();const projects=useQuery<Project[]>({queryKey:['admin-projects'],queryFn:async()=> (await api.get('/projects')).data})
  const logs=useQuery<BuildLog[]>({queryKey:['admin-logs'],queryFn:async()=> (await api.get('/logs')).data})
  const alerts=useQuery<any[]>({queryKey:['admin-alerts'],queryFn:async()=> (await api.get('/admin/notifications')).data})
  const [showOpportunityForm,setShowOpportunityForm]=useState(false)
  const [opportunityError,setOpportunityError]=useState('')
  const addOpportunity=useMutation({
    mutationFn:(body:Partial<Opportunity>)=>api.post('/opportunities',body),
    onSuccess:()=>{setShowOpportunityForm(false);setOpportunityError('');qc.invalidateQueries({queryKey:['admin-alerts']});qc.invalidateQueries({queryKey:['opportunities']})},
    onError:()=>setOpportunityError('Could not add opportunity. Check the form and try again.')
  })
  const removeProject=useMutation({mutationFn:(id:number)=>api.delete(`/projects/${id}`),onSuccess:()=>qc.invalidateQueries({queryKey:['admin-projects']})})
  function logout(){localStorage.removeItem('oziel-token');location.reload()}
  return <div className="page"><header className="page-header"><div><p className="eyebrow">CONTROL ROOM</p><h1>Admin Dashboard</h1></div><button className="secondary" onClick={logout}>Lock system</button></header>
    <div className="admin-grid"><section className="panel admin-table"><div className="panel-head"><h2>Projects</h2><button className="icon-btn"><Plus size={16}/></button></div><table><thead><tr><th>Title</th><th>Status</th><th/></tr></thead><tbody>{(projects.data||[]).map(p=><tr key={p.id}><td>{p.title}</td><td><span className="category">{p.status}</span></td><td><button className="icon-btn danger" onClick={()=>removeProject.mutate(p.id)}><Trash2 size={15}/></button></td></tr>)}</tbody></table></section>
    <section className="panel admin-table"><div className="panel-head"><h2>Build Logs</h2><button className="icon-btn"><Plus size={16}/></button></div><table><thead><tr><th>Title</th><th>Category</th><th/></tr></thead><tbody>{(logs.data||[]).map(l=><tr key={l.id}><td>{l.title}</td><td><span className="category">{l.category}</span></td><td>—</td></tr>)}</tbody></table></section></div>
    <section className="panel admin-table admin-opportunities">
      <div className="panel-head"><div><p className="eyebrow">ALERTS ARE CREATED WHEN A NEW OPPORTUNITY IS ADDED</p><h2>Opportunity inbox</h2></div><button className="primary" onClick={()=>setShowOpportunityForm(true)}><Plus size={15}/> Add opportunity</button></div>
      <table><thead><tr><th>Opportunity</th><th>Company</th><th>Type</th><th>Email alert</th><th>Created</th></tr></thead><tbody>
        {(alerts.data||[]).map(a=><tr key={a.id}><td><b>{a.opportunity}</b></td><td>{a.company}</td><td>{a.channel}</td><td><span className={`category delivery-${a.status.toLowerCase()}`}>{a.status}</span>{a.lastError&&<small className="alert-error">{a.lastError}</small>}</td><td>{new Date(a.createdAt).toLocaleDateString('en-ZA')}</td></tr>)}
      </tbody></table>
      {!alerts.data?.length&&<p className="empty-alerts">No new-opportunity alerts yet. Add a listing to create the first alert.</p>}
    </section>
    {showOpportunityForm&&<div className="modal-backdrop"><form className="modal" onSubmit={e=>{e.preventDefault();const f=new FormData(e.currentTarget);addOpportunity.mutate({name:String(f.get('name')),company:String(f.get('company')),type:String(f.get('type')) as Opportunity['type'],closingDate:String(f.get('closingDate'))||undefined,status:'WISHLIST',link:String(f.get('link'))})}}>
      <button type="button" className="icon-btn" onClick={()=>setShowOpportunityForm(false)}><X size={18}/></button><p className="eyebrow">ADD TO THE OPPORTUNITY TRACKER</p><h2>New opportunity</h2>
      <label>Role or programme<input name="name" required/></label><label>Company / provider<input name="company" required/></label>
      <label>Opportunity type<select name="type"><option>JOB</option><option>INTERNSHIP</option><option>LEARNERSHIP</option><option>BURSARY</option><option>SCHOLARSHIP</option><option>HACKATHON</option></select></label>
      <label>Closing date<input name="closingDate" type="date"/></label><label>Official advert or application link<input name="link" type="url" required/></label>
      {opportunityError&&<p className="error">{opportunityError}</p>}
      <button className="primary" disabled={addOpportunity.isPending}>{addOpportunity.isPending?'Adding and queuing alert...':'Add and notify me'}</button>
    </form></div>}
  </div>
}
